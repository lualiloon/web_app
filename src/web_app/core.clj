(ns web-app.core
  (:require
   [web-app.db :as db]))

(defn -main
  []
  (db/bootstrap))
