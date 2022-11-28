
(ns app.price-quotes.backend.editor.resolvers
    (:require [app.common.backend.api                   :as common]
              [app.price-quotes.backend.handler.helpers :as handler.helpers]
              [com.wsscode.pathom3.connect.operation    :refer [defresolver]]
              [mongo-db.api                             :as mongo-db]
              [pathom.api                               :as pathom]
              [time.api                                 :as time]
              [x.user.api                               :as x.user]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-server-date-f
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (namespaced map)
  ;  {:pathom/target-path (vector)
  ;   :pathom/target-value (string)}
  [_ _]
  (let [timestamp (time/timestamp-string)]
       {:pathom/target-path  [:price-quotes :editor/server-date]
        :pathom/target-value (time/timestamp-string->date timestamp)}))

(defresolver get-server-date
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ;  {:price-quotes.editor/get-server-date (namespaced map)
             ;   {:pathom/target-path (vector)
             ;    :pathom/target-value (string)}}
             [{:keys [request] :as env} resolver-props]
             {:price-quotes.editor/get-server-date (get-server-date-f env resolver-props)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-more-items-price-f
  ; @param (map) env
  ;  {:request (map)}
  ; @param (map) resolver-props
  ;
  ; @return (namespaced map)
  ;  {:pathom/target-path (vector)
  ;   :pathom/target-value (string)}
  [{:keys [request] :as env} _]
  (if (x.user/request->authenticated? request)
      (let [price-quote-item (pathom/env->param env :item)]
           {:pathom/target-path  [:price-quotes :editor/more-items-price]
            :pathom/target-value (handler.helpers/get-more-items-price price-quote-item)})))

(defresolver get-more-items-price
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ;  {:price-quotes.editor/get-more-items-price (namespaced map)
             ;   {:pathom/target-path (vector)
             ;    :pathom/target-value (string)}}
             [{:keys [request] :as env} resolver-props]
             {:price-quotes.editor/get-more-items-price (get-more-items-price-f env resolver-props)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-vehicle-unit-price-f
  ; @param (map) env
  ;  {:request (map)}
  ; @param (map) resolver-props
  ;
  ; @return (namespaced map)
  ;  {:pathom/target-path (vector)
  ;   :pathom/target-value (string)}
  [{:keys [request] :as env} _]
  (if (x.user/request->authenticated? request)
      (let [price-quote-item (pathom/env->param env :item)]
           {:pathom/target-path  [:price-quotes :editor/vehicle-unit-price]
            :pathom/target-value (handler.helpers/get-vehicle-unit-price price-quote-item)})))

(defresolver get-vehicle-unit-price
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ;  {:price-quotes.editor/get-vehicle-unit-price (namespaced map)
             ;   {:pathom/target-path (vector)
             ;    :pathom/target-value (string)}}
             [{:keys [request] :as env} resolver-props]
             {:price-quotes.editor/get-vehicle-unit-price (get-vehicle-unit-price-f env resolver-props)})

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
                   (handler.helpers/unparse-dates item)))))

(defresolver get-item
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ;  {:price-quotes.editor/get-item (namespaced map)}
             [env resolver-props]
             {:price-quotes.editor/get-item (get-item-f env resolver-props)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [get-item get-more-items-price get-vehicle-unit-price get-server-date])

(pathom/reg-handlers! ::handlers HANDLERS)
