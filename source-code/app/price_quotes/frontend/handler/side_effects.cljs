
(ns app.price-quotes.frontend.handler.side-effects
    (:require [ajax.api                                :as ajax]
              [app.price-quotes.frontend.handler.state :as handler.state]
              [app-fruits.blob                         :as blob]
              [app-fruits.base64                       :as base64]
              [mid-fruits.reader                       :as reader]
              [re-frame.api                            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn error-handler-f
  ; @param (keyword) request-id
  ; @param (string) server-response
  [_ _]
  (reset! handler.state/PDF-DOWNLOAD-STATUS :download-error))

(defn response-handler-f
  ; @param (keyword) request-id
  ; @param (string) server-response
  [_ server-response]
  (reset! handler.state/PDF-DOWNLOAD-STATUS :downloaded)
  (reset! handler.state/PDF-OBJECT-URL (-> server-response (reader/string->mixed)
                                                           (:price-quotes.handler/download-pdf)
                                                           (base64/to-blob "application/pdf")
                                                           (blob/to-object-url))))

(defn download-pdf!
  ; @param (namespaced map) price-quote-item
  [price-quote-item]
  (let [resolver-id    :price-quotes.handler/download-pdf
        resolver-props {:item price-quote-item}]
       (reset! handler.state/PDF-DOWNLOAD-STATUS :downloading)
       (r/dispatch [:price-quotes.preview/load-preview!])
       (ajax/send-request! ::download-pdf!
                           {:params             {:query [:pathom/debug `(~resolver-id ~resolver-props)]}
                            :method             :post
                            :error-handler-f    response-handler-f
                            :response-handler-f response-handler-f
                            :uri                "/query"})))

(defn reset-pdf-download!
  []
  (reset! handler.state/PDF-DOWNLOAD-STATUS nil)
  (reset! handler.state/PDF-OBJECT-URL      nil))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-fx :price-quotes.handler/download-pdf!       download-pdf!)
(r/reg-fx :price-quotes.handler/reset-pdf-download! reset-pdf-download!)
