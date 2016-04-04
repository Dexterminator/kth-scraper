(ns kth-scraper.interaction
  (:require [kth-scraper.courses :as courses]
            [clojure.string :as str]
            [kth-scraper.scrape :as scrape]))

(def kth-file "kth-results.html")

(defn- print-choices []
  (println "Choose an option.")
  (println "1. Show courses")
  (println "2. Calculate unweighted GPA")
  (println "3. Calculate weighted GPA")
  (print "> ")
  (flush))

(defn- get-input
  ([] (get-input nil))
  ([default]
   (let [input (clojure.string/trim (read-line))]
     (if (empty? input)
       default
       (clojure.string/lower-case input)))))

(defn- prompt-choice [courses]
  (print-choices)
  (let [input (get-input)
        filtered-courses (courses/courses-with-grade-value courses)]
    (condp = input
      "1" (println (str/join "\n" (map :title courses)))
      "2" (println (courses/unweighted-gpa filtered-courses))
      "3" (println (courses/weighted-gpa filtered-courses))
      (println "Please enter one of the specified digits.")))
  (recur courses))

(defn start []
  (let [courses (doall (scrape/course-infos kth-file))]
    (prompt-choice courses)))
