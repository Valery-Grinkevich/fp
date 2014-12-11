(ns fp2.managers.outputManager
  (:require [fp2.managers.htmlManager :as HtmlManager])
)

(declare CreateMessage)
(declare CalculateIndent)
(declare PrintMessage)
(declare PrintResult)
(declare Print)
(declare GetMessage)

(defn GetMessage [currentNode]
  (let [message (str " " (count (:urls currentNode)))]
    (if (HtmlManager/IsUrlNotNull? (:location currentNode))
      (str message " redirects " (:location currentNode)))
    message
  )
)

(defn CreateMessage [currentNode]
  (if (= (:status currentNode) 404)
    " badUrl"
    (GetMessage currentNode)
  )
)

(defn CalculateIndent [indentCount]
  (apply str (repeat indent "  "))
)

(defn PrintMessage [indent, message, url]
  (println (str (CalculateIndent indent) url message))
)

(defn PrintResult [currentNode, indent]
  (let [currentIndent (* 2 indent)
        url (:url currentNode)
        message (CreateMessage currentNode)]
    (PrintMessage currentIndent message url)
  )
  (doseq [childNode @(:childrenNodes currentNode)] 
  	(PrintResult childNode (inc indent))
  )
)

(defn Print [rootNode]
  (PrintResult rootNode 0)
)