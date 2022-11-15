
(ns app.vehicle-types.backend.handler.helpers
    (:require [mongo-db.api :as mongo-db]
              [pathom.api   :as pathom]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-model-name
  [env]
  (let [model-id (pathom/env->param env :model-id)]
       (if-let [model-item (mongo-db/get-document-by-id "vehicle_models" model-id)]
               (:model/name model-item))))
