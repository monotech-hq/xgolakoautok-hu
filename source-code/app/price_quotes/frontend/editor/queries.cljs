
(ns app.price-quotes.frontend.editor.queries
    (:require [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-server-date-query
  ; @usage
  ;  (request-server-date-query)
  ;
  ; @return (vector)
  []
  [`(~:price-quotes.editor/get-server-date ~{})])

(defn request-more-items-price-query
  ; @usage
  ;  (request-more-items-price-query)
  ;
  ; @return (vector)
  []
  (let [price-quote-packages @(r/subscribe [:db/get-item [:price-quotes :editor/edited-item :packages]])
        price-quote-products @(r/subscribe [:db/get-item [:price-quotes :editor/edited-item :products]])
        price-quote-services @(r/subscribe [:db/get-item [:price-quotes :editor/edited-item :services]])]
       [`(~:price-quotes.editor/get-more-items-price ~{:item {:price-quote/packages price-quote-packages
                                                              :price-quote/products price-quote-products
                                                              :price-quote/services price-quote-services}})]))

(defn request-vehicle-unit-price-query
  ; @usage
  ;  (request-vehicle-unit-price-query)
  ;
  ; @return (vector)
  []
  (let [price-quote-type @(r/subscribe [:db/get-item [:price-quotes :editor/edited-item :type]])]
       [`(~:price-quotes.editor/get-vehicle-unit-price ~{:item {:price-quote/type price-quote-type}})]))
