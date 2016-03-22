(ns kth-scraper.scrape
  (:require [net.cgrand.enlive-html :as html]
            [clojure.string :as str]))

(def grade-values
  {"A" 5.0
   "B" 4.5
   "C" 4.0
   "D" 3.5
   "E" 3.0})

(def kth-file "kth-results.html")

(defn credit-value
  [credit-string]
  (Double/parseDouble (str/replace (re-find #"\d+,\d+" credit-string) #"," ".")))

(defn fetch-file
  []
  (html/html-resource kth-file))

(defn courses
  []
  (html/select (fetch-file) [:tr.course]))

(defn course-info
  [course]
  {:title       (str/trim (first (html/select course [(html/nth-child 2) html/text-node])))
   :course-code (first (html/select course [html/first-child :a html/text-node]))
   :grade       (first (html/select course [:.grade html/text-node]))
   :credits     (credit-value (first (html/select course [:.credits html/text-node])))
   :link        (:href (:attrs (first (html/select course [html/first-child :a]))))
   :completed   (.contains (:class (:attrs course)) "complete")})

(defn unweighted-gpa
  [course-infos]
  (let [completed (filter #(and (:completed %) (contains? grade-values (:grade %))) course-infos)
        grade-sum (reduce #(+ %1 (get grade-values (:grade %2) 0)) 0 completed)]
    (/ grade-sum (count completed))))

(def course-infos (map course-info (courses)))
course-infos
(unweighted-gpa course-infos)
