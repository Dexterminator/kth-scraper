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

(defn fetch-courses
  []
  (html/select (fetch-file) [:tr.course]))

(defn course-info
  [course]
  {:title       (str/trim (first (html/select course [(html/nth-child 2) html/text-node])))
   :course-code (first (html/select course [html/first-child :a html/text-node]))
   :grade       (first (html/select course [:.grade html/text-node]))
   :credits     (credit-value (first (html/select course [:.credits html/text-node])))
   :link        (:href (:attrs (first (html/select course [html/first-child :a]))))
   :completed   (not (.contains (:class (:attrs course)) "incomplete"))})

(def course-infos (map course-info (fetch-courses)))
(def courses-by-completion (group-by :completed course-infos))
(def completed (get courses-by-completion true []))
(def not-completed (get courses-by-completion false []))
(def completed-by-grade (group-by :grade completed))
(def courses-with-grade-value (flatten (vals (dissoc completed-by-grade "P"))))

(defn unweighted-gpa
  [courses]
  (let [grade-sum (reduce (fn [sum course] (+ sum (get grade-values (:grade course)))) 0 courses)]
    (/ grade-sum (count courses))))

(defn weighted-gpa
  [courses]
  (let [weighted-grade-sum (reduce (fn [sum course]
                                    (+ sum (* (:credits course) (get grade-values (:grade course))))) 0 courses)
        credit-sum (reduce (fn [sum course] (+ sum (:credits course))) 0 courses)]
    (/ weighted-grade-sum credit-sum)))

(unweighted-gpa courses-with-grade-value)
(weighted-gpa courses-with-grade-value)
