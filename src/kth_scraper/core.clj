(ns kth-scraper.core
  (:require [kth-scraper.scrape :as scrape])
  (:gen-class))

(def kth-file "kth-results.html")

(defn -main
  [& args]
  (println (scrape/course-infos kth-file)))

