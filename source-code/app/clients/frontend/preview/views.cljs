
(ns app.clients.frontend.preview.views
    (:require [app.common.frontend.api                 :as common]
              [app.clients.frontend.preview.prototypes :as preview.prototypes]
              [app.components.frontend.api             :as components]
              [elements.api                            :as elements]
              [random.api                              :as random]
              [re-frame.api                            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-preview-data
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :item-link (namespaced map)
  ;    {:client/id (string)}}
  [_ {{:client/keys [id]} :item-link :keys [disabled?]}]
  (let [client-name          @(r/subscribe [:clients.preview/get-client-name                   id])
        client-address       @(r/subscribe [:clients.preview/get-client-address                id])
        client-email-address @(r/subscribe [:x.db/get-item [:clients :preview/downloaded-items id :email-address]])
        client-phone-number  @(r/subscribe [:x.db/get-item [:clients :preview/downloaded-items id :phone-number]])
        client-company-name  @(r/subscribe [:x.db/get-item [:clients :preview/downloaded-items id :company-name]])
        client-vat-no        @(r/subscribe [:x.db/get-item [:clients :preview/downloaded-items id :vat-no]])]
       [components/data-table {:disabled? disabled?
                               :rows [[{:content :name}
                                       {:content client-name          :color :muted :copyable? true :selectable? true :placeholder :unnamed-client}]
                                      [{:content :email-address}
                                       {:content client-email-address :color :muted :copyable? true :selectable? true :placeholder "n/a"}]
                                      [{:content :phone-number}
                                       {:content client-phone-number  :color :muted :copyable? true :selectable? true :placeholder "n/a"}]
                                      [{:content :address}
                                       {:content client-address       :color :muted :copyable? true :selectable? true :placeholder "n/a"}]
                                      [{:content :company-name}
                                       {:content client-company-name  :color :muted :copyable? true :selectable? true :placeholder "n/a"}]
                                      [{:content :vat-no}
                                       {:content client-vat-no        :color :muted :copyable? true :selectable? true :placeholder "n/a"}]]}]))

(defn- client-avatar
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :item-link (namespaced map)
  ;     {:client/id (string)}}
  [_ {{:client/keys [id]} :item-link :keys [disabled?]}]
  (let [client-colors     @(r/subscribe [:x.db/get-item [:clients :preview/downloaded-items id :colors]])
        client-first-name @(r/subscribe [:x.db/get-item [:clients :preview/downloaded-items id :first-name]])
        client-last-name  @(r/subscribe [:x.db/get-item [:clients :preview/downloaded-items id :last-name]])]
       ; XXX#0059
       ; A user-avatar komponens méreteiben megegyezik a többi modul preview almoduljának
       ; thumbnail komponenseivel.
       [components/user-avatar {:colors     client-colors
                                :disabled?  disabled?
                                :first-name client-first-name
                                :last-name  client-last-name
                                :style {:align-items     "center"
                                        :display         "flex"
                                        :height          "120px"
                                        :justify-content "center"
                                        :width           "144px"}}]))

(defn- client-preview-element
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  [preview-id preview-props]
  [:div {:style {:display "flex" :flex-wrap "wrap" :grid-gap "12px"}}
        [client-avatar       preview-id preview-props]
        [client-preview-data preview-id preview-props]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-preview
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  [preview-id preview-props]
  [common/item-preview preview-id (assoc preview-props :preview-element #'client-preview-element)])

(defn element
  ; @param (keyword)(opt) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :indent (map)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :item-link (namespaced map)(opt)
  ;    {:client/id (string)}
  ;   :label (metamorphic-content)(opt)
  ;   :placeholder (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [client-preview {...}]
  ;
  ; @usage
  ;  [client-preview :my-client-preview {...}]
  ([preview-props]
   [element (random/generate-keyword) preview-props])

  ([preview-id preview-props]
   (let [preview-props (preview.prototypes/preview-props-prototype preview-id preview-props)]
        [client-preview preview-id preview-props])))
