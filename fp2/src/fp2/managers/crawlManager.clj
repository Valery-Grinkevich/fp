(ns fp2.managers.crawlManager
  (:require [fp2.managers.htmlManager :as HtmlManager]
  )
)

(declare RunTreeTraversal)
(declare CreateNewNode)
(declare GetRootNode)
(declare Crawl)
(declare GetChildNode)
(declare ParseHtml)

(defn ParseHtml [parentNode, url, depth]
  (let [htmlPageCode (HtmlManager/GetPageContent url)
        status (:status htmlPageCode)
        urlsFromHtml(HtmlManager/ParseUrlsFromHtml htmlPageCode)
        currentLocation (HtmlManager/GetLocation htmlPageCode)
        ]
    (GetChildNode parentNode depth url urlsFromHtml currentLocation status)
  )
)

(defn GetChildNode [parentNode, depth, url, urls, location, status]
  (let [childNode(CreateNewNode parentNode (atom []) depth url urls location status)]
    (swap! (:childrenNodes parentNode) conj childNode)
    childNode
  )
)

(defn RunTreeTraversal [currentNode, urls, depth]
  (let [currentCrawlDepth (dec depth)]
    (if (not= depth 0)
      (doseq [childNode (pmap #(ParseHtml currentNode % depth) urls)]
        (RunTreeTraversal childNode (:urls childNode) currentCrawlDepth)
      )
      currentNode
    )
  )
)

(defn CreateNewNode [parentNode, childrenNodes, depth, url, urls, location, status]
  {:parentNode parentNode 
   :childrenNodes childrenNodes 
   :depth depth 
   :url url 
   :urls urls 
   :location location 
   :status status
  }
)

(defn GetRootNode [depth, urls]
  (CreateNewNode nil (atom []) depth "RootNode" urls nil nil)
)

(defn Crawl [crawlDepth, crawlUrls]
  (let [rootNode (GetRootNode crawlDepth crawlUrls)]
    (RunTreeTraversal rootNode crawlUrls crawlDepth)
    rootNode
  )
)