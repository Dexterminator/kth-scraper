(ns kth-scraper.core
  (:require [kth-scraper.interaction :as interaction])
  (:gen-class))

(defn -main
  [& args]
  (interaction/start)
  (shutdown-agents))
