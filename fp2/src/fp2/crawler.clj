(ns fp2.crawler
  (:gen-class)
  (:require [fp2.managers.crawlManager :as CrawlManager]
  )
)

(defn -main
  [& args]
  (if (= (count args) 1)
    (let [depth (last args)]
      (CrawlManager/Crawl depth)
    )
    (println "Incorrect number of arguments!")
   )
 )