
(ns app.price-quotes.backend.viewer.resolvers
    (:require [app.common.backend.api                   :as common]
              [app.price-quotes.backend.handler.helpers :as handler.helpers]
              [com.wsscode.pathom3.connect.operation    :refer [defresolver]]
              [mid-fruits.candy                         :refer [return]]
              [mid-fruits.keyword                       :as keyword]
              [mid-fruits.map                           :as map]
              [math.api                          :as math]
              [mongo-db.api                             :as mongo-db]
              [pathom.api                               :as pathom]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item-f
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (namespaced map)
  [env _]
  (let [item-id    (pathom/env->param env :item-id)
        projection (common/get-document-projection :price-quote)]
       (if-let [item (mongo-db/get-document-by-id "price_quotes" item-id projection)]
               (assoc item :price-quote/more-items-price   (-> item handler.helpers/get-more-items-price)
                           :price-quote/vehicle-unit-price (-> item handler.helpers/get-vehicle-unit-price)))))

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
