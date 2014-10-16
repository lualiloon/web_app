(ns web-app.db
  (:require
   [datomic.api :as d]
   [crypto.password.bcrypt :as b]))


(def uri "datomic:dev://localhost:4334/web-app")
(def conn (d/connect uri))

;; Initialize the database
;; NOTE: only initialize once, then comment out the code.
(defn initialize-db
  [uri]
  (when (d/create-database uri)
    (let [schema (read-string (slurp "resources/public/edn/schema.edn"))]
      @(d/transact conn schema))))


(defn add-user
  [username password]
  (let [;; encrypt also takes an optional work factor
        hashed (b/encrypt password)
        
        data [{:db/id #db/id[:db.part/user]
               :user/username username
               :user/password hashed}]]
    
    @(d/transact conn data)))


(defn check-password
  [username password]
  (b/check password
           (ffirst (d/q '{:find [?pwd]
                          :with [?usr]
                          :where [[?id :user/username ?usr]
                                  [?id :user/password ?pwd]]}
                        (d/db conn) username))))


;; Update value in database
;; FIXME: write the update function


(defn bootstrap
  []
  (initialize-db uri))



