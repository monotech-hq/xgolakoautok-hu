
(ns app.website-menus.backend.editor.prototypes
    (:require [re-frame.api :as r]
              [string.api   :as string]
              [uri.api      :as uri]
              [vector.api   :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn added-document-prototype
  ; @param (namespaced map) document
  ;
  ; @return (namespaced map)
  [document]
  (letfn [(f [{:keys [link] :as item-props}]
             ; "/my-page"      => "https://my-domain.com/my-page"
             ; "something.com" => "https://something.com"
             ; "my-page"       => "https://my-domain.com/my-page"
             (let [app-domain @(r/subscribe [:x.core/get-app-config-item :app-domain])]
                  (cond (string/starts-with?   link "/") (update item-props :link #(str app-domain     %))
                        (string/contains-part? link ".") (update item-props :link #(uri/valid-uri      %))
                        :else                            (update item-props :link #(str app-domain "/" %)))))]
         (update document :menu/menu-items vector/->items f)))

(defn updated-document-prototype
  ; @param (namespaced map) document
  ;
  ; @return (namespaced map)
  [document]
  (added-document-prototype document))
