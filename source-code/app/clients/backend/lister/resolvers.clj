
(ns app.clients.backend.lister.resolvers
    (:require [app.clients.backend.lister.helpers    :as lister.helpers]
              [app.common.backend.api                :as common]
              [com.wsscode.pathom3.connect.operation :refer [defresolver]]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]
              [x.user.api                            :as x.user]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-items-f
  ; @param (map) env
  ;  {:request (map)}
  ; @param (map) resolver-props
  ;
  ; @return (map)
  ;  {:document-count (integer)
  ;   :documents (namespaced maps in vector)}
  [{:keys [request] :as env} _]
  (if (x.user/request->authenticated? request)
      (let [get-pipeline   (lister.helpers/env->get-pipeline   env)
            count-pipeline (lister.helpers/env->count-pipeline env)]
           {:items          (mongo-db/get-documents-by-pipeline   "clients" get-pipeline)
            :all-item-count (mongo-db/count-documents-by-pipeline "clients" count-pipeline)})))

(defresolver get-items
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ;  {:clients.lister/get-items (map)
             ;    {:document-count (integer)
             ;     :documents (namespaced maps in vector)}}
             [env resolver-props]
             {:clients.lister/get-items (get-items-f env resolver-props)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [get-items])

(pathom/reg-handlers! ::handlers HANDLERS)
