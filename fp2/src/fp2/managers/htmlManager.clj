(ns fp2.managers.htmlManager
  (:require [clj-http.client :as client]
            [net.cgrand.enlive-html :as html]
  )
)

(declare ParseUrlsFromHtml)
(declare IsUrlNotNull)
(declare RemoveNulls)
(declare GetPageContent)
(declare GetLocation)
(declare CheckStatus)

(defn GetLocation [htmlPageCode]
  (let [status (:status htmlPageCode)]
    (if (boolean (CheckStatus status))
      (:location (:headers htmlPageCode))
      nil
    )
  )
)

(defn GetPageContent [url]
  (try
  	(client/get url)
  	(catch Exception e {:status 404})
  )
)

(defn CheckStatus [status]
  (some #(= status %) [300 301 302 303 307])
)

(defn ParseUrlsFromHtml [htmlPageCode]
  (RemoveNulls (map #(:href (:attrs %)) (html/select htmlPageCode #{[:a]})))
)

(defn IsUrlNotNull? [url]
  (not (nil? url))
)

(defn RemoveNulls [urls]
  (filter IsUrlNotNull? urls)
)