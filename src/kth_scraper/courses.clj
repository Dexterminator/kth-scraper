(ns kth-scraper.courses
  (:import (java.text DecimalFormat)))

(def grade-values
  {"A" 5.0
   "B" 4.5
   "C" 4.0
   "D" 3.5
   "E" 3.0})

(defn courses-with-grade-value [courses]
  (filter #(and (:completed %) (contains? grade-values (:grade %))) courses))

(defn- gpa-round [gpa]
  (.format (DecimalFormat. "0.00") gpa))

(defn unweighted-gpa [courses]
  (let [grade-sum (reduce (fn [sum course] (+ sum (grade-values (:grade course)))) 0 courses)]
    (gpa-round (/ grade-sum (count courses)))))

(defn weighted-gpa [courses]
  (let [weighted-grade-sum (reduce (fn [sum course]
                                     (+ sum (* (:credits course) (grade-values (:grade course))))) 0 courses)
        credit-sum (reduce (fn [sum course] (+ sum (:credits course))) 0 courses)]
    (gpa-round (/ weighted-grade-sum credit-sum))))
