(ns fp1.clusterization
  (:gen-class))
(require '[clojure.string :as str])
(require '[clojure.java.io :as io])

(def Ra 3.0)
(def Rb(* 1.5 Ra))
(def EpsMax 0.5)
(def Emin 0.15)
(def FilePath "./resources/glass.txt")

(def Alpha (/ 4 (Math/pow Ra 2)))
(def Beta (/ 4 (Math/pow Rb 2)))

(defn CalculateEuclideanDistance [firstPoint, secondPoint]
  {:pre [(= (count firstPoint) (count secondPoint))]}
  (defn sqr [n] (* n n))
  (reduce + (map sqr (map - firstPoint secondPoint)))
)

(defn CalculateHammingDistance [firstPoint, secondPoint]
  {:pre [(= (count firstPoint) (count secondPoint))]}
  (count (filter true? (map not= firstPoint secondPoint)))
)

(defn CreatePoint [line]
  {:coords (vec (map #(Double/parseDouble %) (drop-last (.split #"," line)))) :potential 0.0}
)

(defn ReadFile []
  {:pre [(not (nil? FilePath))]}
  (if (.exists (io/file FilePath))
    (with-open [file (io/reader FilePath)]
      (doall (map CreatePoint (line-seq file)))
    )
    (println ("ERROR. File (" FilePath ") doesn't exist"))
  )
)

(defn GetPointPotential [point, currentPoint, distanceFunction]
  (Math/pow Math/E (* (- Alpha) (distanceFunction (point :coords) (currentPoint :coords))))
)

(defn GetCommonPotential [point, points, distanceFunction]
  (reduce (fn [res currentPoint]
            (+ res (GetPointPotential point currentPoint distanceFunction))
          ) 0.0 points)
)

(defn CalculatePotential [point, points, distanceFunction]
  (let [newPotencial (GetCommonPotential point points distanceFunction)]
    (assoc point :potential newPotencial)
  )
)

(defn CalculatePotentials [points, distanceFunction]
  (map #(CalculatePotential % points distanceFunction) points)
)

(defn CorrectPotential [point, basePoint, distanceFunction]
  (let [correctedPotential
        (- (point :potential) (* (basePoint :potential) (Math/pow Math/E (* (- Beta) (distanceFunction (point :coords) (basePoint :coords))))))]
    (assoc point :potential correctedPotential)
  )
)

(defn CorrectPotentials [points, basePoint, distanceFunction]
  (map #(CorrectPotential % basePoint distanceFunction) points)
)

(defn GetMaxPotentialPoint [points]
  (apply max-key (fn [point] (point :potential)) points)
)

(defn GetMinDistance [point, points, distanceFunction]
  (Math/sqrt (apply min (map #(distanceFunction (point :coords) (% :coords)) points)))
)

(defn EquatePotentialToZero [point, currentPoint]
  (if (= (point :coords) (currentPoint :coords))
    (assoc point :potential 0.0)
  point)
)

(defn EquatePotenrialsToZero [points, currentPoint]
  (map #(EquatePotentialToZero % currentPoint) points)
)

(defn Clusterize [points, firstClusterPotential, centers, distanceFunction]
  (let [maxPoint (GetMaxPotentialPoint points)
        correctedPotenrials (CorrectPotentials points maxPoint distanceFunction)]

    (cond
      (> (maxPoint :potential) (* EpsMax firstClusterPotential)) (recur correctedPotenrials firstClusterPotential (conj centers maxPoint) distanceFunction)
      (< (maxPoint :potential) (* Emin firstClusterPotential)) centers
      (>= ((/ (GetMinDistance maxPoint centers distanceFunction) Ra) (/ (maxPoint :potential) firstClusterPotential)) 1) (recur correctedPotenrials firstClusterPotential (conj centers maxPoint) distanceFunction)
      :else (recur (EquatePotenrialsToZero correctedPotenrials maxPoint) firstClusterPotential centers distanceFunction)
    )
  )
)

(defn GetPoints [distanceFunction]
  (let [points (ReadFile)]
    (CalculatePotentials points distanceFunction)
  )
)

(defn RunClasterization [distanceFunction]
  (let [points (GetPoints distanceFunction)
        maxPotentialPoint (GetMaxPotentialPoint points)
        firstCluster (maxPotentialPoint :potential)]
    (doseq [clusters (Clusterize points firstCluster [] distanceFunction)] (println clusters))
  )
)

(defn -main [& args]
  (if (>= (count args) 1)
    (let [distanceFunctionType (first args)
          distanceFunction (case distanceFunctionType
                              "h" CalculateHammingDistance
                              "e" CalculateEuclideanDistance)
          ]
      (RunClasterization distanceFunction)
    )
    (println "Not enough params has been received.")
  )
)
