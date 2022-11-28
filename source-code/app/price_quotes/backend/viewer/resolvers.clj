
(ns app.price-quotes.backend.viewer.resolvers
    (:require [app.common.backend.api                   :as common]
              [app.price-quotes.backend.handler.helpers :as handler.helpers]
              [candy.api                                :refer [return]]
              [com.wsscode.pathom3.connect.operation    :refer [defresolver]]
              [keyword.api                              :as keyword]
              [map.api                                  :as map]
              [math.api                                 :as math]
              [mongo-db.api                             :as mongo-db]
              [pathom.api                               :as pathom]
              [x.user.api                               :as x.user]))

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
           (if-let [item (mongo-db/get-document-by-id "price_quotes" item-id {:prototype-f prototype-f})]
                   (assoc item :price-quote/more-items-price   (-> item handler.helpers/get-more-items-price)
                               :price-quote/vehicle-unit-price (-> item handler.helpers/get-vehicle-unit-price))))))

(defresolver get-item
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ;  {:price-quotes.viewer/get-item (namespaced map)}
             [env resolver-props]
             {:price-quotes.viewer/get-item (get-item-f env resolver-props)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [get-item])

(pathom/reg-handlers! ::handlers HANDLERS)
