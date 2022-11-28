
(ns app.storage.frontend.media-picker.views
    (:require [app.common.frontend.api                      :as common]
              [app.components.frontend.api                  :as components]
              [app.storage.frontend.media-picker.prototypes :as media-picker.prototypes]
              [app.storage.frontend.media-preview.views     :as media-preview.views]
              [elements.api                                 :as elements]
              [format.api                                   :as format]
              [io.api                                       :as io]
              [random.api                                   :as random]
              [re-frame.api                                 :as r]
              [x.media.api                                  :as x.media]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- directory-list-item
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ; @param (integer) item-dex
  ; @param (namespaced map) media-link
  ; @param (map) drag-props
  [picker-id picker-props item-dex media-link drag-props])
  ; TODO

(defn- file-list-item
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:sortable? (boolean)(opt)}
  ; @param (integer) item-dex
  ; @param (namespaced map) media-link
  ;  {:media/id (string)}
  ; @param (map) drag-props
  ;  {:handle-attributes (map)
  ;   :item-attributes (map)}
  [picker-id {:keys [sortable?]} item-dex {:media/keys [count id]} {:keys [handle-attributes item-attributes]}]
  (let [{:keys [alias filename modified-at size thumbnail]} @(r/subscribe [:item-lister/get-item picker-id id])
        timestamp   @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
        size         (-> size io/B->MB format/decimals (str " MB"))]
       [components/item-list-row {:drag-attributes item-attributes
                                  :cells [(if sortable? [components/list-item-drag-handle {:drag-attributes handle-attributes}])
                                          (if sortable? [components/list-item-gap         {:width 12}])
                                          ; XXX#6690 (source-code/app/storage/media_browser/views.cljs)
                                          (cond (io/filename->audio? alias)
                                                [components/list-item-thumbnail {:icon :audio_file :icon-family :material-icons-outlined}]
                                                (io/filename->image? alias)
                                                [components/list-item-thumbnail {:thumbnail (x.media/filename->media-thumbnail-uri filename)}]
                                                (io/filename->text?  alias)
                                                [components/list-item-thumbnail {:icon :insert_drive_file :icon-family :material-icons-outlined}]
                                                (io/filename->video? alias)
                                                [components/list-item-thumbnail {:icon :video_file :icon-family :material-icons-outlined}]
                                                :else
                                                [components/list-item-thumbnail {:icon :insert_drive_file :icon-family :material-icons-outlined}])
                                          [components/list-item-gap       {:width 12}]
                                          [components/list-item-cell      {:rows [{:content alias :placeholder :unnamed-file}]}]
                                          [components/list-item-gap       {:width 12}]
                                          [components/list-item-cell      {:rows [{:content size :font-size :xs :color :muted}] :width 100}]
                                          [components/list-item-gap       {:width 12}]
                                          [components/list-item-cell      {:rows [{:content timestamp :font-size :xs :color :muted}] :width 100}]]
                                  :border (if (not= item-dex 0) :top)}]))

(defn- media-list-item
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ; @param (integer) item-dex
  ; @param (map) media-link
  ; @param (map)(opt) drag-props
  ([picker-id picker-props item-dex media-link]
   [media-list-item picker-id picker-props item-dex media-link {}])

  ([picker-id picker-props item-dex {:media/keys [id] :as media-link} drag-props]
   (let [{:keys [mime-type]} @(r/subscribe [:item-lister/get-item picker-id id])]
        (case mime-type "storage/directory" [directory-list-item picker-id picker-props item-dex media-link drag-props]
                                            [file-list-item      picker-id picker-props item-dex media-link drag-props]))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- media-item
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ; @param (namespaced map) media-link
  [picker-id picker-props media-link]
  (let [preview-props (media-picker.prototypes/preview-props-prototype picker-id picker-props media-link)]
       [media-preview.views/element picker-id preview-props]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- media-list-header
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:sortable? (boolean)(opt)}
  [_ {:keys [sortable?]}]
  [components/item-list-header ::media-list-header
                               {:cells [(if sortable? {:width 24})
                                        (if sortable? {:width 12})
                                        {:width 84}
                                        {:width 12}
                                        {:label :name}
                                        {:width 12}
                                        {:label :size :width 100}
                                        {:width 12}
                                        {:label :modified :width 100}]
                                :border :bottom}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- media-picker
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  [picker-id picker-props]
  [common/item-picker picker-id (assoc picker-props :item-element      #'media-item
                                                    :list-item-element #'media-list-item
                                                    :item-list-header  #'media-list-header)])

(defn element
  ; @param (keyword)(opt) picker-id
  ; @param (map) picker-props
  ;  {:autosave? (boolean)(opt)
  ;    Default: false
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :extensions (strings in vector)(opt)
  ;   :indent (map)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :max-count (integer)(opt)
  ;    Default: 8
  ;   :multi-select? (boolean)(opt)
  ;    Default: false
  ;   :on-save (metamorphic-event)(opt)
  ;    Az esemény utolsó paraméterként megkapja a kiválasztott elemet.
  ;   :placeholder (metamorphic-content)(opt)
  ;   :required? (boolean)(opt)
  ;    Default: false
  ;   :sortable? (boolean)(opt)
  ;    Default: false
  ;   :toggle-label (metamorphic-content)(opt)
  ;   :value-path (vector)}
  ;
  ; @usage
  ;  [media-picker {...}]
  ;
  ; @usage
  ;  [media-picker :my-media-picker {...}]
  ([picker-props]
   [element (random/generate-keyword) picker-props])

  ([picker-id picker-props]
   (let [picker-props (media-picker.prototypes/picker-props-prototype picker-id picker-props)]
        [media-picker picker-id picker-props])))
