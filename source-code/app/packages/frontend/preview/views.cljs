
(ns app.packages.frontend.preview.views
    (:require [app.common.frontend.api                  :as common]
              [app.components.frontend.api              :as components]
              [app.packages.frontend.preview.prototypes :as preview.prototypes]
              [elements.api                             :as elements]
              [random.api                               :as random]
              [re-frame.api                             :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- package-preview-data
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :item-link (namespaced map)
  ;    {:package/count (integer)
  ;     :package/id (string)}}
  [_ {{:package/keys [count id]} :item-link :keys [disabled?]}]
  (let [package-name                @(r/subscribe [:x.db/get-item [:packages :preview/downloaded-items id :name]])
        package-item-number         @(r/subscribe [:x.db/get-item [:packages :preview/downloaded-items id :item-number]])
        package-unit-price          @(r/subscribe [:x.db/get-item [:packages :preview/downloaded-items id :unit-price] 0])
        package-quantity-unit-value @(r/subscribe [:x.db/get-item [:packages :preview/downloaded-items id :quantity-unit :value]])
        package-quantity             {:content package-quantity-unit-value :replacements [count]}]
       [components/data-table {:disabled? disabled?
                               :rows [[          {:content :name}        {:content package-name                    :color :muted :selectable? true :placeholder :unnamed-package}]
                                      [          {:content :item-number} {:content package-item-number             :color :muted :selectable? true :placeholder "n/a"}]
                                      (if count [{:content :quantity}    {:content package-quantity                :color :muted :selectable? true :placeholder "n/a"}])
                                      [          {:content :unit-price}  {:content (str package-unit-price " EUR") :color :muted :selectable? true :placeholder "n/a"}]]}]))

(defn- package-preview-thumbnail
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  [_ {{:package/keys [id]} :item-link :keys [disabled?]}]
  ; XXX#0059 (source-code/app/clients/frontend/preview/views.cljs)
  (let [thumbnail @(r/subscribe [:x.db/get-item [:packages :preview/downloaded-items id :thumbnail]])]
       [elements/thumbnail ::package-preview-thumbnail
                           {:border-radius :s
                            :disabled?     disabled?
                            :height        :3xl
                            :width         :5xl
                            :uri           (:media/uri thumbnail)}]))

(defn- package-preview-element
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  [preview-id preview-props]
  [:div {:style {:display "flex" :flex-wrap "wrap" :grid-gap "12px" :align-items "flex-start"}}
        [package-preview-thumbnail preview-id preview-props]
        [package-preview-data      preview-id preview-props]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- package-preview
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  [preview-id preview-props]
  [common/item-preview preview-id (assoc preview-props :preview-element #'package-preview-element)])

(defn element
  ; @param (keyword)(opt) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :indent (map)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :item-link (namespaced map)(opt)
  ;    {:package/count (integer)
  ;     :package/id (string)}
  ;   :label (metamorphic-content)(opt)
  ;   :placeholder (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [package-preview {...}]
  ;
  ; @usage
  ;  [package-preview :my-package-preview {...}]
  ([preview-props]
   [element (random/generate-keyword) preview-props])

  ([preview-id preview-props]
   (let [preview-props (preview.prototypes/preview-props-prototype preview-id preview-props)]
        [package-preview preview-id preview-props])))
