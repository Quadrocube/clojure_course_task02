(ns clojure-course-task02.core
  (:gen-class)
  (:require [clojure.java.io :as io])
  (:use clojure.pprint))

(defn pget-content [dir]
  (cons dir
    (mapcat deref 
      (map #(future (pget-content %)) (.listFiles dir)))))


(defn find-files [file-name path]
  "Parallel Search for a file using his name as a regexp."
  (->> path 
    io/file
    pget-content
    (pmap #(.getName %))
    (filter #(re-matches (re-pattern file-name) %))))

(defn usage []
  (println "Usage: $ run.sh file_name path"))

(defn -main [file-name path]
  (if (or (nil? file-name)
          (nil? path))
    (usage)
    (do
      (println "Searching for " file-name " in " path "...")
      (dorun (map println (find-files file-name path)))
      (shutdown-agents))))
