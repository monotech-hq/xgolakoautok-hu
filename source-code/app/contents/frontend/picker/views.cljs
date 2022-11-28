
(ns app.contents.frontend.picker.views
    (:require [app.common.frontend.api                 :as common]
              [app.components.frontend.api             :as components]
              [app.contents.frontend.handler.helpers   :as handler.helpers]
              [app.contents.frontend.picker.prototypes :as picker.prototypes]
              [app.contents.frontend.preview.views     :as preview.views]
              [elements.api                            :as elements]
              [hiccup.api                              :as hiccup]
              [random.api                              :as random]
              [re-frame.api                            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-list-item
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:sortable? (boolean)(opt)}
  ; @param (integer) item-dex
  ; @param (namespaced map) content-link
  ;  {:content/id (string)}
  ; @param (map)(opt) drag-props
  ;  {:handle-attributes (map)
  ;   :item-attributes (map)}
  ([picker-id picker-props item-dex content-link]
   [content-list-item picker-id picker-props item-dex content-link {}])

  ([picker-id {:keys [sortable?]} item-dex {:content/keys [id]} {:keys [handle-attributes item-attributes]}]
   (let [{:keys [body modified-at name id]} @(r/subscribe [:item-lister/get-item picker-id id])
         timestamp   @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
         content-body (-> body handler.helpers/parse-content-body hiccup/to-string)]
        [components/item-list-row {:drag-attributes item-attributes
                                   :cells [(if sortable? [components/list-item-drag-handle {:drag-attributes handle-attributes}])
                                           [components/list-item-thumbnail {:icon :article :icon-family :material-icons-outlined}]
                                           [components/list-item-cell      {:rows [{:content name :placeholder :unnamed-content}
                                                                                   {:content content-body :font-size :xs :color :muted}]}]
                                           [components/list-item-gap       {:width 12}]
                                           [components/list-item-cell      {:rows [{:content timestamp :font-size :xs :color :muted}] :width 100}]]
                                   :border (if (not= item-dex 0) :top)}])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-item
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ; @param (namespaced map) content-link
  [picker-id picker-props content-link]
  (let [preview-props (picker.prototypes/preview-props-prototype picker-id picker-props content-link)]
       [preview.views/element picker-id preview-props]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-list-header
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:sortable? (boolean)(opt)}
  [_ {:keys [sortable?]}]
  [components/item-list-header ::content-list-header
                               {:cells [(if sortable? {:width 24})
                                        {:width 84}
                                        {:label :name}
                                        {:width 12}
                                        {:label :modified :width 100}]
                                :border :bottom}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-picker
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  [picker-id picker-props]
  [common/item-picker picker-id (assoc picker-props :item-element      #'content-item
                                                    :list-item-element #'content-list-item
                                                    :item-list-header  #'content-list-header)])

(defn element
  ; @param (keyword)(opt) picker-id
  ; @param (map) picker-props
  ;  {:autosave? (boolean)(opt)
  ;    Default: false
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :indent (map)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :max-count (integer)(opt)
  ;    Default: 8
  ;    W/ {:multi-select? true}
  ;   :max-lines (integer)(opt)
  ;   :multi-select? (boolean)(opt)
  ;    Default: false
  ;   :on-change (metamorphic-event)(opt)
  ;    Az esemény utolsó paraméterként megkapja a kiválasztott elemet.
  ;   :on-save (metamorphic-event)(opt)
  ;    Az esemény utolsó paraméterként megkapja a kiválasztott elemet.
  ;   :placeholder (metamorphic-content)(opt)
  ;   :required? (boolean)(opt)
  ;    Default: false
  ;   :sortable? (boolean)(opt)
  ;    Default: false
  ;    W/ {:multi-select? true}
  ;   :toggle-label (metamorphic-content)(opt)
  ;   :value-path (vector)}
  ;
  ; @usage
  ;  [content-picker {...}]
  ;
  ; @usage
  ;  [content-picker :my-content-picker {...}]
  ([picker-props]
   [element (random/generate-keyword) picker-props])

  ([picker-id picker-props]
   (let [picker-props (picker.prototypes/picker-props-prototype picker-id picker-props)]
        [content-picker picker-id picker-props])))
