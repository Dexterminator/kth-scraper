(ns kth-scraper.courses-test
  (:require [clojure.test :refer :all]
            [kth-scraper.courses :refer :all]))

(def filtered-courses
  [{:title       "course1"
     :course-code "DD4711"
     :grade       "A"
     :credits     7.5
     :link        "abc.com"
     :completed   true}
    {:title       "course2"
     :course-code "DD4712"
     :grade       "B"
     :credits     6.0
     :link        "abc.com"
     :completed   true}
    {:title       "course3"
     :course-code "DD4713"
     :grade       "C"
     :credits     6.0
     :link        "abc.com"
     :completed   true}
    {:title       "course5"
     :course-code "DD4714"
     :grade       "D"
     :credits     15.0
     :link        "abc.com"
     :completed   true}])

(def courses (conj filtered-courses
                   {:title       "course4"
                    :course-code "DD4714"
                    :grade       nil
                    :credits     6.0
                    :link        "abc.com"
                    :completed   false}
                   {:title       "course4"
                    :course-code "DD4714"
                    :grade       "P"
                    :credits     6.0
                    :link        "abc.com"
                    :completed   true}))

(deftest test-courses-with-grade-value
  (is (= (courses-with-grade-value courses))))

(deftest test-unweighted-gpa
  (is (= (unweighted-gpa filtered-courses) "4.25")))

(deftest test-weighted-gpa
  (is (= (weighted-gpa filtered-courses) "4.09")))
