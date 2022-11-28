
(ns app.price-quote-templates.backend.editor.prototypes
    (:require [app.price-quote-templates.mid.handler.config :as handler.config]
              [candy.api                                    :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn added-document-prototype
  ; @param (namespaced map) document
  ;
  ; @return (namespaced map)
  ;  {:template/default-currency (string)
  ;   :template/language (keyword)}
  [document]
  ; XXX#5050 (source-code/app/price_quote_templates/frontend/README.md)
  (merge {:template/default-currency handler.config/DEFAULT-DEFAULT-CURRENCY
          :template/language         handler.config/DEFAULT-LANGUAGE}
         (param document)))

(defn updated-document-prototype
  ; @param (namespaced map) document
  ;
  ; @return (namespaced map)
  ;  {:template/default-currency (string)
  ;   :template/language (keyword)}
  [document]
  ; XXX#5050 (source-code/app/price_quote_templates/frontend/README.md)
  (merge {:template/default-currency handler.config/DEFAULT-DEFAULT-CURRENCY
          :template/language         handler.config/DEFAULT-LANGUAGE}
         (param document)))
