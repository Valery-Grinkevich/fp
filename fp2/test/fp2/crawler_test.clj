(ns fp1.core-test
  (:require [clojure.test :refer :all]
            [fp2.core :refer :all]))

(deftest TestIsReditected
  (testing "Testing redirection"
  	(let [httpResponse (HtmlManager/GetPageContent "http://asb.by")]
	  (is (boolean (HtmlManager/CheckStatus (:status responce))))
  	)
  )
)

(deftest TestNotFound
  (testing "Testring "
    (let [httpResponse (HtmlManager/GetPageContent "http://www.getnetgoing.com/demo/errors/NotFound.html")]
      (is (= 404 (:status response)))
    )
  )
)
