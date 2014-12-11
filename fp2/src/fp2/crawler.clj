(ns fp2.crawler
  (:gen-class)
  (:require [fp2.managers.ioManager :as IoManager]
            [fp2.managers.crawlManager :as CrawlManager]
            [fp2.managers.outputManager :as OutputManager]
  )
)

(defn -main
  [& args]
  (if (= (count args) 1)
    (let [urlsFromFile (IoManager/ReadFile)
          depth (last args)
          rootNode (CrawlManager/Crawl depth urlsFromFile)]
      (OutputManager/Print rootNode))
    (println "Incorrect number of arguments!")
   )
 )