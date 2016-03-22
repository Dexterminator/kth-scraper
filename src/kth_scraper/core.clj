(ns kth-scraper.core
  (:require [kth-scraper.scrape :as scrape])
  (:gen-class))

(defn -main
  [& args]
  (println scrape/fetch-file))

