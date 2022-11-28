
(ns app.packages.backend.viewer.resolvers
    (:require [app.common.backend.api                :as common]
              [app.packages.backend.handler.helpers  :as handler.helpers]
              [com.wsscode.pathom3.connect.operation :refer [defresolver]]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]
              [x.user.api                            :as x.user]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-automatic-price-f
  ; @param (map) env
  ;  {:request (map)}
  ; @param (map) resolver-props
  ;
  ; @return (integer)
  [{:keys [request] :as env} _]
  (if (x.user/request->authenticated? request)
      (let [package-id (pathom/env->param env :package-id)]
           {:pathom/target-path  [:packages :viewer/viewed-item :automatic-price]
            :pathom/target-value (handler.helpers/get-package-price {:package/count 1 :package/id package-id})})))

(defresolver get-automatic-price
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ;  {:packages.viewer/get-automatic-price (integer)}
             [env resolver-props]
             {:packages.viewer/get-automatic-price (get-automatic-price-f env resolver-props)})

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
           (mongo-db/get-document-by-id "packages" item-id {:prototype-f prototype-f}))))

(defresolver get-item
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ;  {:packages.viewer/get-item (namespaced map)}
             [env resolver-props]
             {:packages.viewer/get-item (get-item-f env resolver-props)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [get-automatic-price get-item])

(pathom/reg-handlers! ::handlers HANDLERS)
