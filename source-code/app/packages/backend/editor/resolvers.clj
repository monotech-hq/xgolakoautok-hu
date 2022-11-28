
(ns app.packages.backend.editor.resolvers
    (:require [app.common.backend.api                :as common]
              [app.packages.backend.handler.helpers  :as handler.helpers]
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
      (let [item-id      (pathom/env->param env :item-id)
            prototype-f #(common/get-document-prototype request %)]
           (if-let [item (mongo-db/get-document-by-id "packages" item-id {:prototype-f prototype-f})]
                   (let [automatic-price (handler.helpers/get-package-price item-id)]
                        (assoc item :package/automatic-price automatic-price))))))

(defresolver get-item
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ;  {:packages.editor/get-item (namespaced map)}
             [env resolver-props]
             {:packages.editor/get-item (get-item-f env resolver-props)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [get-item])

(pathom/reg-handlers! ::handlers HANDLERS)
