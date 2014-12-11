(defproject fp2 "0.1.0-SNAPSHOT"
  :description "Crawler"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
  				 [enlive "1.1.5"]
			     [clj-http "1.0.1"]
			    ]
  :main ^:skip-aot fp2.crawler
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})