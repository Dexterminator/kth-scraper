(ns kth-scraper.scrape
  (:require [net.cgrand.enlive-html :as html]
            [clojure.string :as str])
  (:import (java.net URL)))

(def level-tranlations
  {"First cycle"    :first-cycle
   "Second cycle"   :second-cycle
   "Grundnivå"      :first-cycle
   "Avancerad nivå" :second-cycle})

(defn- fetch-courses [file]
  (html/select (html/html-resource file) [:tr.course]))

(defn- fetch-url [url]
  (html/html-resource (URL. url)))

(defn- credit-value [credit-string]
  (Double/parseDouble (str/replace (re-find #"\d+,\d+" credit-string) #"," ".")))

(defn- add-detail-info [course]
  (let [page (fetch-url (:link course))
        level ((comp level-tranlations str/trim second :content first) (html/select page [:ul.infoset :li]))]
    (assoc course :level level)))

(defn- course-info [course]
  (let [basic-course-info {:title       (str/trim (first (html/select course [(html/nth-child 2) html/text-node])))
                           :course-code (first (html/select course [html/first-child :a html/text-node]))
                           :grade       (first (html/select course [:.grade html/text-node]))
                           :credits     (credit-value (first (html/select course [:.credits html/text-node])))
                           :link        (:href (:attrs (first (html/select course [html/first-child :a]))))
                           :completed   (not (.contains (:class (:attrs course)) "incomplete"))}]
    (add-detail-info basic-course-info)))

(defn course-infos [file]
  (pmap course-info (fetch-courses file)))
