
(ns app.vehicle-types.backend.preview.resolvers
    (:require [app.common.backend.api                :as common]
              [com.wsscode.pathom3.connect.operation :refer [defresolver]]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]
              [x.user.api                            :as x.user]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item-f
  ; @param (map) env
  ;  {:request (map)}
  ; @param (map) resolver-props
  ;
  ; @return (namespaced map)
  [{:keys [request] :as env} _]
  (if (x.user/request->authenticated? request)
      (let [item-id (pathom/env->param env :item-id)]
           (mongo-db/get-document-by-id "vehicle_types" item-id))))

(defresolver get-item
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ;  {:vehicle-types.preview/get-item (namespaced map)}
             [env resolver-props]
             {:vehicle-types.preview/get-item (get-item-f env resolver-props)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [get-item])

(pathom/reg-handlers! ::handlers HANDLERS)
