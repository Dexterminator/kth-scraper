(ns kth-scraper.core
  (:require [kth-scraper.scrape :as scrape]
            [kth-scraper.courses :as courses])
  (:gen-class))

(def kth-file "kth-results.html")

(defn -main
  [& args]
  (println (courses/unweighted-gpa (courses/courses-with-grade-value (scrape/course-infos scrape/fetch-courses kth-file)))))

