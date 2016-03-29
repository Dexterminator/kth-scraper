(ns kth-scraper.scrape
  (:require [net.cgrand.enlive-html :as html]
            [clojure.string :as str]))

(defn fetch-courses [file]
  (html/select (html/html-resource file) [:tr.course]))

(defn credit-value [credit-string]
  (Double/parseDouble (str/replace (re-find #"\d+,\d+" credit-string) #"," ".")))

(defn course-info [course]
  {:title       (str/trim (first (html/select course [(html/nth-child 2) html/text-node])))
   :course-code (first (html/select course [html/first-child :a html/text-node]))
   :grade       (first (html/select course [:.grade html/text-node]))
   :credits     (credit-value (first (html/select course [:.credits html/text-node])))
   :link        (:href (:attrs (first (html/select course [html/first-child :a]))))
   :completed   (not (.contains (:class (:attrs course)) "incomplete"))})

(defn course-infos [fetch-courses-fn file]
  (map course-info (fetch-courses-fn file)))
