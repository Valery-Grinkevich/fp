(ns fp2.managers.ioManager
  (:require [clojure.java.io :as io])
)

(def FilePath "./resources/urls.txt")

(defn ReadFile []
  (if (.exists (io/file FilePath))
  	(clojure.string/split-lines (slurp FilePath))
  	(println ("ERROR. File (" FilePath ") doesn't exist"))
  )
)