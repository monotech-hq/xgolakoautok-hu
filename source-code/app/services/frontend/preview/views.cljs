
(ns app.services.frontend.preview.views
    (:require [app.common.frontend.api                  :as common]
              [app.components.frontend.api              :as components]
              [app.services.frontend.preview.prototypes :as preview.prototypes]
              [elements.api                             :as elements]
              [random.api                               :as random]
              [re-frame.api                             :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- service-preview-data
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :item-link (namespaced map)
  ;    {:service/count (integer)
  ;     :service/id (string)}}
  [_ {{:service/keys [count id]} :item-link :keys [disabled?]}]
  (let [service-name                @(r/subscribe [:x.db/get-item [:services :preview/downloaded-items id :name]])
        service-item-number         @(r/subscribe [:x.db/get-item [:services :preview/downloaded-items id :item-number]])
        service-unit-price          @(r/subscribe [:x.db/get-item [:services :preview/downloaded-items id :unit-price] 0])
        service-quantity-unit-value @(r/subscribe [:x.db/get-item [:services :preview/downloaded-items id :quantity-unit :value]])
        service-quantity             {:content service-quantity-unit-value :replacements [count]}]
       [components/data-table {:disabled? disabled?
                               :rows [[          {:content :name}        {:content service-name                    :color :muted :selectable? true :placeholder :unnamed-service}]
                                      [          {:content :item-number} {:content service-item-number             :color :muted :selectable? true :placeholder "n/a"}]
                                      (if count [{:content :quantity}    {:content service-quantity                :color :muted :selectable? true :placeholder "n/a"}])
                                      [          {:content :unit-price}  {:content (str service-unit-price " EUR") :color :muted :selectable? true :placeholder "n/a"}]]}]))

(defn- service-preview-thumbnail
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {}
  [_ {{:service/keys [id]} :item-link :keys [disabled?]}]
  ; XXX#0059 (source-code/app/clients/frontend/preview/views.cljs)
  (let [thumbnail @(r/subscribe [:x.db/get-item [:services :preview/downloaded-items id :thumbnail]])]
       [elements/thumbnail ::service-preview-thumbnail
                           {:border-radius :s
                            :disabled?     disabled?
                            :height        :3xl
                            :width         :5xl
                            :uri           (:media/uri thumbnail)}]))

(defn- service-preview-element
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  [preview-id preview-props]
  [:div {:style {:display "flex" :flex-wrap "wrap" :grid-gap "12px" :align-items "flex-start"}}
        [service-preview-thumbnail preview-id preview-props]
        [service-preview-data      preview-id preview-props]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- service-preview
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  [preview-id preview-props]
  [common/item-preview preview-id (assoc preview-props :preview-element #'service-preview-element)])

(defn element
  ; @param (keyword)(opt) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :indent (map)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :item-link (namespaced maps in vector)(opt)
  ;    {:service/count (integer)
  ;     :service/id (string)}
  ;   :label (metamorphic-content)(opt)
  ;   :placeholder (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [service-preview {...}]
  ;
  ; @usage
  ;  [service-preview :my-service-preview {...}]
  ([preview-props]
   [element (random/generate-keyword) preview-props])

  ([preview-id preview-props]
   (let [preview-props (preview.prototypes/preview-props-prototype preview-id preview-props)]
        [service-preview preview-id preview-props])))
