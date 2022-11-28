
(ns app.price-quote-templates.frontend.preview.views
    (:require [app.common.frontend.api                               :as common]
              [app.components.frontend.api                           :as components]
              [app.price-quote-templates.frontend.preview.prototypes :as preview.prototypes]
              [elements.api                                          :as elements]
              [random.api                                            :as random]
              [re-frame.api                                          :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-preview-data
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :item-link (namespaced map)
  ;    {:template/count (integer)
  ;     :template/id (string)}}
  [_ {{:template/keys [id]} :item-link :keys [disabled?]}]
  (let [template-name             @(r/subscribe [:x.db/get-item [:price-quote-templates :preview/downloaded-items id :name]])
        template-issuer-name      @(r/subscribe [:x.db/get-item [:price-quote-templates :preview/downloaded-items id :issuer-name]])
        template-default-currency @(r/subscribe [:x.db/get-item [:price-quote-templates :preview/downloaded-items id :default-currency]])
        template-language         @(r/subscribe [:x.db/get-item [:price-quote-templates :preview/downloaded-items id :language]])]
       [components/data-table {:disabled? disabled?
                               :rows [[{:content :name}              {:content template-name             :color :muted :selectable? true :placeholder :unnamed-price-quote-template}]
                                      [{:content :issuer-name}       {:content template-issuer-name      :color :muted :selectable? true :placeholder "n/a"}]
                                      [{:content :default-currency}  {:content template-default-currency :color :muted :selectable? true :placeholder "n/a"}]
                                      [{:content :language}          {:content template-language         :color :muted :selectable? true :placeholder "n/a"}]]}]))

(defn- template-preview-thumbnail
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {}
  ; @param (namespaced map) template-link
  ;  {:template/id (string)}
  [_ {{:template/keys [id]} :item-link :keys [disabled?]}]
  ; XXX#0059 (source-code/app/clients/frontend/preview/views.cljs)
  (let [issuer-logo @(r/subscribe [:x.db/get-item [:price-quote-templates :preview/downloaded-items id :issuer-logo]])]
       [elements/thumbnail ::template-preview-thumbnail
                           {:border-radius :s
                            :disabled?     disabled?
                            :height        :3xl
                            :width         :5xl
                            :uri           (:media/uri issuer-logo)}]))

(defn- template-preview-element
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  [preview-id preview-props]
  [:div {:style {:display "flex" :flex-wrap "wrap" :grid-gap "12px" :align-items "flex-start"}}
        [template-preview-thumbnail preview-id preview-props]
        [template-preview-data      preview-id preview-props]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-preview
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  [preview-id preview-props]
  [common/item-preview preview-id (assoc preview-props :preview-element #'template-preview-element)])

(defn element
  ; @param (keyword)(opt) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :indent (map)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :item-link (namespaced maps in vector)(opt)
  ;    {:template/id (string)
  ;   :label (metamorphic-content)(opt)
  ;   :placeholder (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [template-preview {...}]
  ;
  ; @usage
  ;  [template-preview :my-template-preview {...}]
  ([preview-props]
   [element (random/generate-keyword) preview-props])

  ([preview-id preview-props]
   (let [preview-props (preview.prototypes/preview-props-prototype preview-id preview-props)]
        [template-preview preview-id preview-props])))
