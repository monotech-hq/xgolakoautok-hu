
(ns app.clients.backend.lister.helpers
    (:require [engines.item-lister.api :as item-lister]
              [pathom.api              :as pathom]
              [x.locales.api    :as x.locales]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->name-field-pattern
  [{:keys [request]}]
  ; A :client/first-name és :client/last-name tulajdonságok sorrendjéhez a felhasználó által
  ; kiválaszott nyelv szerinti sorrendet alkalmazza.
  (let [name-order (x.locales/request->name-order request)]
       (case name-order :reversed {:client/name {:$concat [:$client/last-name  " " :$client/first-name]}}
                                  {:client/name {:$concat [:$client/first-name " " :$client/last-name]}})))

(defn env->get-pipeline
  [env]
  ; XXX#7601
  ; A :client/name virtuális mezőt szükséges hozzáadni a dokumentumokhoz a keresés és rendezés előtt!
  (let [name-field-pattern (env->name-field-pattern env)
        env                (pathom/env<-param env :field-pattern name-field-pattern)]
       (item-lister/env->get-pipeline env :clients.lister)))

(defn env->count-pipeline
  [env]
  (let [name-field-pattern (env->name-field-pattern env)
        env (pathom/env<-param env :field-pattern name-field-pattern)]
       (item-lister/env->count-pipeline env :clients.lister)))
