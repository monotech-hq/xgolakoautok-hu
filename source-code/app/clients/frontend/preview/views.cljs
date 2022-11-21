
(ns app.clients.frontend.preview.views
    (:require [app.common.frontend.api                 :as common]
              [app.clients.frontend.preview.prototypes :as preview.prototypes]
              [app.components.frontend.api             :as components]
              [elements.api                            :as elements]
              [engines.item-preview.api                :as item-preview]
              [random.api                              :as random]
              [re-frame.api                            :as r]
              [vector.api                              :as vector]

              ; TEMP
              [plugins.dnd-kit.api :as dnd-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-preview-data
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)}
  ; @param (namespaced map) client-link
  ;  {:id (string)}
  [_ {:keys [disabled?] :as preview-props} {:client/keys [id]}]
  (let [client-name          @(r/subscribe [:clients.preview/get-client-name id])
        client-email-address @(r/subscribe [:x.db/get-item [:clients :preview/downloaded-items id :phone-number]])
        client-phone-number  @(r/subscribe [:x.db/get-item [:clients :preview/downloaded-items id :phone-number]])
        client-address       @(r/subscribe [:clients.preview/get-client-address              id])
        client-company-name  @(r/subscribe [:x.db/get-item [:clients :preview/downloaded-items id :company-name]])
        client-vat-no        @(r/subscribe [:x.db/get-item [:clients :preview/downloaded-items id :vat-no]])]
       [common/data-table {:disabled? disabled?
                           :rows [[{:content :name}
                                   {:content client-name          :color :muted :placeholder :unnamed-client}]
                                  [{:content :email-address}
                                   {:content client-email-address :color :muted :placeholder "-"}]
                                  [{:content :phone-number}
                                   {:content client-phone-number  :color :muted :placeholder "-"}]
                                  [{:content :address}
                                   {:content client-address       :color :muted :placeholder "-"}]
                                  [{:content :company-name}
                                   {:content client-company-name  :color :muted :placeholder "-"}]
                                  [{:content :vat-no}
                                   {:content client-vat-no        :color :muted :placeholder "-"}]]}]))

(defn- client-avatar
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)}
  ; @param (namespaced map) client-link
  ;  {:client/id (string)}
  [_ {:keys [disabled?]} {:client/keys [id]}]
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
                                        :height          "96px"
                                        :justify-content "center"
                                        :width           "120px"}}]))

(defn- client-preview-element
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ; @param (namespaced map) client-link
  [preview-id preview-props client-link]
  [:div {:style {:display "flex" :flex-wrap "wrap" :grid-gap "12px"}}
        [client-avatar       preview-id preview-props client-link]
        [client-preview-data preview-id preview-props client-link]])

(defn- client-preview-static-body
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ; @param (namespaced map) client-link
  ;  {}
  [preview-id preview-props {:client/keys [id] :as client-link}]
  ; BUG#9980 (app.products.frontend.preview.views)
  ; BUG#8871 (app.products.frontend.preview.views)
  (if id [item-preview/body (keyword id)
                            {:error-element   [common/error-element {:error :the-content-has-been-broken}]
                             :ghost-element   #'common/item-preview-ghost-element
                             :preview-element [client-preview-element preview-id preview-props client-link]
                             :item-id         id
                             :item-path       [:clients :preview/downloaded-items id]
                             :transfer-id     :clients.preview}]))

(defn- client-preview-sortable-body
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ; @param (integer) item-dex
  ; @param (namespaced map) client-link
  ;  {:client/id (string)}
  ; @param (map) drag-props
  ;  {:handle-attributes (map)
  ;   :item-attributes (map)
  [preview-id preview-props item-dex {:client/keys [id] :as client-link} {:keys [handle-attributes item-attributes]}]
  [:div (update item-attributes :style merge {:align-items "center" :display "flex" :grid-column-gap "18px"})
        (if @(r/subscribe [:item-preview/data-received? (keyword id)])
             [components/list-item-drag-handle {:drag-attributes handle-attributes}])
        [client-preview-static-body preview-id preview-props client-link]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-preview-static-list
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:items (namespaced maps in vector)}
  [preview-id {:keys [items] :as preview-props}]
  (letfn [(f [preview-list client-link] (conj preview-list [client-preview-static-body preview-id preview-props client-link]))]
         (reduce f [:div {:style {:display "flex" :flex-direction "column" :grid-row-gap "24px"}}] items)))

(defn- client-preview-sortable-list
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:items (namespaced maps in vector)
  ;   :value-path (vector)}
  [preview-id {:keys [items value-path] :as preview-props}]
  [:div {:style {:display "flex" :flex-direction "column" :grid-row-gap "24px"}}
        [dnd-kit/body preview-id
                      {:common-props     preview-props
                       :items            items
                       :item-id-f        :client/id
                       :item-element     #'client-preview-sortable-body
                       :on-order-changed (fn [_ _ %] (r/dispatch-sync [:x.db/set-item! value-path %]))}]])

(defn- client-preview-list
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:sortable? (boolean)(opt)}
  [preview-id {:keys [sortable?] :as preview-props}]
  (if sortable? [client-preview-sortable-list preview-id preview-props]
                [client-preview-static-list   preview-id preview-props]))

(defn- client-preview-label
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)}
  [_ {:keys [disabled? info-text label]}]
  (if label [elements/label {:content     label
                             :disabled?   disabled?
                             :info-text   info-text
                             :line-height :block}]))

(defn- client-preview-placeholder
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :placeholder (metamorphic-content)(opt)}
  [_ {:keys [disabled? placeholder]}]
  (if placeholder [elements/label {:color       :muted
                                   :content     placeholder
                                   :disabled?   disabled?
                                   :font-size   :xs
                                   :line-height :block
                                   :selectable? true}]))

(defn- client-preview
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:indent (map)(opt)
  ;   :items (namespaced maps in vector)(opt)}
  [preview-id {:keys [indent items] :as preview-props}]
  [elements/blank preview-id
                  {:content [:<> [client-preview-label preview-id preview-props]
                                 (if (vector/nonempty?           items)
                                     [client-preview-list        preview-id preview-props]
                                     [client-preview-placeholder preview-id preview-props])]
                   :indent  indent}])

(defn element
  ; @param (keyword)(opt) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :indent (map)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :items (namespaced maps in vector)(opt)
  ;    [{:client/id (string)}
  ;     {...}]
  ;   :max-count (integer)(opt)
  ;    Default: 8
  ;   :label (metamorphic-content)(opt)
  ;   :placeholder (metamorphic-content)(opt)
  ;   :sortable? (boolean)(opt)
  ;    Default: false
  ;   :value-path (vector)(opt)
  ;    W/ {:sortable? true}}
  ;
  ; @usage
  ;  [client-preview {...}]
  ;
  ; @usage
  ;  [client-preview :my-preview {...}]
  ([preview-props]
   [element (random/generate-keyword) preview-props])

  ([preview-id preview-props]
   (let [preview-props (preview.prototypes/preview-props-prototype preview-id preview-props)]
        [client-preview preview-id preview-props])))
