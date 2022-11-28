
(ns app.storage.frontend.media-preview.views
    (:require [app.storage.frontend.media-preview.prototypes :as media-preview.prototypes]
              [app.common.frontend.api                       :as common]
              [app.components.frontend.api                   :as components]
              [elements.api                                  :as elements]
              [io.api                                        :as io]
              [math.api                                      :as math]
              [random.api                                    :as random]
              [re-frame.api                                  :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- media-preview-data
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :item-link (namespaced map)
  ;    {:media/id (string)}}
  [_ {{:media/keys [id]} :item-link :keys [disabled?]}]
  (let [media-alias       @(r/subscribe [:x.db/get-item [:storage :media-preview/downloaded-items id :alias]])
        media-size        @(r/subscribe [:x.db/get-item [:storage :media-preview/downloaded-items id :size]])
        media-uploaded-at @(r/subscribe [:x.db/get-item [:storage :media-preview/downloaded-items id :added-at]])
        timestamp         @(r/subscribe [:x.activities/get-actual-timestamp media-uploaded-at])
        media-size        {:content :n-kb :replacements [(-> media-size io/B->kB math/round)]}]
       [components/data-table {:disabled? disabled?
                               :rows [[{:content :filename} {:content media-alias :color :muted :selectable? true :placeholder "n/a"}]
                                      [{:content :filesize} {:content media-size  :color :muted :selectable? true :placeholder "n/a"}]
                                      [{:content :uploaded} {:content timestamp   :color :muted :selectable? true :placeholder "n/a"}]]}]))

(defn- media-preview-image-thumbnail
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :item-link (namespaced map)
  ;    {:media/id (string)
  ;     :media/uri}}
  [_ {{:media/keys [id uri]} :item-link :keys [disabled?]}]
  ; XXX#0059 (source-code/app/clients/frontend/preview/views.cljs)
  ; XXX#6690 (source-code/app/storage/media_browser/views.cljs)
  (let [thumbnail @(r/subscribe [:x.db/get-item [:storage :media-preview/downloaded-items id :thumbnail]])]
       [elements/thumbnail {:border-radius :s
                            :disabled? disabled?
                            :uri       uri
                            :height    :3xl
                            :width     :5xl}]))

(defn- media-preview-file-thumbnail
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :item-link (namespaced map)
  ;    {:media/id (string)}}
  [_ {{:media/keys [id]} :item-link :keys [disabled?]}]
  ; XXX#0059 (source-code/app/clients/frontend/preview/views.cljs)
  ; XXX#6690 (source-code/app/storage/media_browser/views.cljs)
  (let [alias @(r/subscribe [:x.db/get-item [:storage :media-preview/downloaded-items id :alias]])
        icon (cond (io/filename->audio? alias) :audio_file
                   (io/filename->text?  alias) :insert_drive_file
                   (io/filename->video? alias) :video_file
                   :else :insert_drive_file)]
       [elements/thumbnail {:border-radius :s
                            :disabled?   disabled?
                            :icon        icon
                            :icon-family :material-icons-outlined
                            :height      :3xl
                            :width       :5xl}]))

(defn- media-preview-thumbnail
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :item-link (namespaced map)
  ;    {:media/id (string)}}
  [preview-id {{:media/keys [id]} :item-link :keys [disabled?] :as preview-props}]
  ; XXX#6690 (source-code/app/storage/media_browser/views.cljs)
  (let [alias @(r/subscribe [:x.db/get-item [:storage :media-preview/downloaded-items id :alias]])]
       (if (io/filename->image? alias)
           [media-preview-image-thumbnail preview-id preview-props]
           [media-preview-file-thumbnail  preview-id preview-props])))

(defn- media-preview-element
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  [preview-id preview-props]
  [:div {:style {:display "flex" :flex-wrap "wrap" :grid-gap "12px" :align-items "flex-start"}}
        [media-preview-thumbnail preview-id preview-props]
        [media-preview-data      preview-id preview-props]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- media-preview-empty-thumbnail
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :thumbnail (map)}
  [preview-id {:keys [disabled? thumbnail]}]
  [elements/thumbnail {:border-radius :s
                       :disabled?     disabled?
                       :height        (:height thumbnail)
                       :width         (:width  thumbnail)}])

(defn- media-preview
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:item-link (namespaced map)(opt)
  ;    {:media/id (string)}
  ;   :placeholder (metamorphic-content)(opt)}
  [preview-id {:keys [placeholder] :as preview-props}]
  ; XXX#9010
  (if (= placeholder :empty-thumbnail)
      [common/item-preview preview-id (assoc preview-props :preview-element #'media-preview-element
                                                           :placeholder     #'media-preview-empty-thumbnail)]
      [common/item-preview preview-id (assoc preview-props :preview-element #'media-preview-element)]))

(defn element
  ; @param (keyword)(opt) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :indent (map)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :item-link (namespaced maps in vector)(opt)
  ;    {:media/id (string)
  ;     :media/uri (string)}
  ;   :label (metamorphic-content)(opt)
  ;   :placeholder (metamorphic-content)(opt)
  ;    :empty-thumbnail}
  ;
  ; @usage
  ;  [media-preview {...}]
  ;
  ; @usage
  ;  [media-preview :my-media-preview {...}]
  ([preview-props]
   [element (random/generate-keyword) preview-props])

  ([preview-id preview-props]
   (let [preview-props (media-preview.prototypes/preview-props-prototype preview-id preview-props)]
        [media-preview preview-id preview-props])))
