(ns kth-scraper.core
  (:require [kth-scraper.scrape :as scrape])
  (:gen-class))

(defn -main
  [& args]
  (println (map scrape/course-info (scrape/fetch-courses "kth-results.html"))))

