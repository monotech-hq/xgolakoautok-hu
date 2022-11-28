
(ns app.storage.frontend.media-selector.views
    (:require [app.common.frontend.api          :as common]
              [app.components.frontend.api      :as components]
              [app.storage.frontend.core.config :as core.config]
              [elements.api                     :as elements]
              [format.api                       :as format]
              [io.api                           :as io]
              [layouts.popup-a.api              :as popup-a]
              [re-frame.api                     :as r]
              [x.components.api                 :as x.components]
              [x.media.api                      :as x.media]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  ; @param (keyword) popup-id
  [_]
  (let [selected-item-count            @(r/subscribe [:item-browser/get-selected-item-count        :storage.media-selector])
        all-downloaded-media-selected? @(r/subscribe [:item-browser/all-downloaded-items-selected? :storage.media-selector])
        any-downloaded-media-selected? @(r/subscribe [:item-browser/any-downloaded-item-selected?  :storage.media-selector])
        on-discard-selection [:item-browser/discard-selection! :storage.media-selector]]
       [common/item-selector-footer :storage.media-selector
                                    {:on-discard-selection          on-discard-selection
                                     :all-downloaded-item-selected? all-downloaded-media-selected?
                                     :any-downloaded-item-selected? any-downloaded-media-selected?
                                     :selected-item-count           selected-item-count}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- open-directory-icon-button
  ; @param (integer) item-dex
  ; @param (map) media-item
  [_ {:keys [id]}]
  [:th {:style {:width "72px"}}
       [elements/icon-button {:icon        :navigate_next
                              :on-click    [:item-browser/browse-item! :storage.media-selector id]
                              :hover-color :highlight}]])

(defn- directory-list-item
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; @param (integer) item-dex
  ; @param (map) media-item
  [selector-id _ item-dex {:keys [alias size items modified-at] :as media-item}]
  (let [timestamp  @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
        size        (str (-> size io/B->MB format/decimals (str " MB\u00A0\u00A0\u00A0|\u00A0\u00A0\u00A0"))
                         (x.components/content {:content :n-items :replacements [(count items)]}))
        icon-family (if (empty? items) :material-icons-outlined :material-icons-filled)]
       [components/item-list-row {:cells [[components/list-item-gap       {:width 12}]
                                          [components/list-item-thumbnail {:icon :folder :icon-family icon-family}]
                                          [components/list-item-gap       {:width 12}]
                                          [components/list-item-cell      {:rows [{:content alias :placeholder :unnamed-file}
                                                                                  {:content size :font-size :xs :color :muted}
                                                                                  {:content timestamp :font-size :xs :color :muted}]}]
                                          [components/list-item-gap       {:width 6}]
                                          [open-directory-icon-button item-dex media-item]
                                          [components/list-item-gap       {:width 6}]]
                                  :border (if (not= item-dex 0) :top)}]))

(defn- file-list-item
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; @param (integer) item-dex
  ; @param (map) media-item
  [selector-id _ item-dex {:keys [alias id modified-at filename size] :as media-item}]
  (let [file-selectable? @(r/subscribe [:storage.media-selector/file-selectable? media-item])
        timestamp        @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
        size              (-> size io/B->MB format/decimals (str " MB"))]
       [components/item-list-row {:cells [[components/list-item-gap       {:width 12}]
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
                                          [components/list-item-cell      {:rows [{:content alias :placeholder :unnamed-file}
                                                                                  {:content size :font-size :xs :color :muted}
                                                                                  {:content timestamp :font-size :xs :color :muted}]}]
                                          [components/list-item-gap {:width 6}]
                                          [common/selector-item-marker selector-id item-dex {:item-id id :disabled? (not file-selectable?)}]
                                          [components/list-item-gap {:width 6}]]
                                  :border    (if (not= item-dex 0) :top)
                                  :disabled? (not file-selectable?)}]))

(defn- media-list-item
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; @param (integer) item-dex
  ; @param (map) media-item
  [selector-id selector-props item-dex {:keys [mime-type] :as media-item}]
  (case mime-type "storage/directory" [directory-list-item selector-id selector-props item-dex media-item]
                                      [file-list-item      selector-id selector-props item-dex media-item]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  ; @param (keyword) popup-id
  [_]
  [:<> [elements/horizontal-separator {:height :xs}]
       [common/item-selector-body :storage.media-selector
                                  {:default-item-id   core.config/ROOT-DIRECTORY-ID
                                   :item-path         [:storage :media-selector/browsed-item]
                                   :items-path        [:storage :media-selector/downloaded-items]
                                   :list-item-element #'media-list-item
                                   :engine            :item-browser
                                   :items-key         :items
                                   :label-key         :alias
                                   :path-key          :path}]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- order-by-icon-button
  []
  (let [browser-disabled? @(r/subscribe [:item-browser/browser-disabled? :storage.media-selector])
        order-by-options   [:modified-at/ascending :modified-at/descending :alias/ascending :alias/descending]
        on-click           [:item-browser/choose-order-by! :storage.media-selector {:order-by-options order-by-options}]]
       [elements/icon-button ::order-by-icon-button
                             {:disabled?   browser-disabled?
                              :hover-color :highlight
                              :on-click    on-click
                              :preset      :order-by}]))

(defn- upload-files-icon-button
  []
  (let [browser-disabled? @(r/subscribe [:item-browser/browser-disabled? :storage.media-selector])]
       [elements/icon-button ::upload-files-icon-button
                             {:disabled?   browser-disabled?
                              :hover-color :highlight
                              :on-click    [:storage.media-selector/upload-files!]
                              :icon        :upload_file}]))

(defn- create-folder-icon-button
  []
  (let [browser-disabled? @(r/subscribe [:item-browser/browser-disabled? :storage.media-selector])]
       [elements/icon-button ::create-folder-icon-button
                             {:disabled?   browser-disabled?
                              :hover-color :highlight
                              :on-click    [:storage.media-selector/create-directory!]
                              :icon        :create_new_folder}]))

(defn- go-home-icon-button
  []
  (let [browser-disabled? @(r/subscribe [:item-browser/browser-disabled? :storage.media-selector])
        at-home?          @(r/subscribe [:item-browser/at-home?          :storage.media-selector])
        error-mode?       @(r/subscribe [:item-browser/get-meta-item     :storage.media-selector :error-mode?])]
       [elements/icon-button ::go-home-icon-button
                             {:disabled?   (or browser-disabled? (and at-home? (not error-mode?)))
                              :hover-color :highlight
                              :on-click    [:item-browser/go-home! :storage.media-selector]
                              :preset      :home}]))

(defn- go-up-icon-button
  []
  (let [browser-disabled? @(r/subscribe [:item-browser/browser-disabled? :storage.media-selector])
        at-home?          @(r/subscribe [:item-browser/at-home?          :storage.media-selector])]
       [elements/icon-button ::go-up-icon-button
                             {:disabled?   (or browser-disabled? at-home?)
                              :hover-color :highlight
                              :on-click    [:item-browser/go-up! :storage.media-selector]
                              :preset      :back}]))

(defn- search-items-field
  []
  (let [search-event [:item-browser/search-items! :storage.media-selector {:search-keys [:alias]}]]
       [elements/search-field ::search-items-field
                              {:autoclear?    true
                               :indent        {:left :s :right :xxs :top :xxs}
                               :on-empty      search-event
                               :on-type-ended search-event
                               :placeholder   :search-in-the-directory
                               :search-keys   [:alias]}]))

(defn- control-bar
  []
  (if-let [first-data-received? @(r/subscribe [:item-browser/first-data-received? :storage.media-selector])]
          [:div {:style {:display "flex"}}
                [:div {:style {:display "flex"}}
                      [go-up-icon-button]
                      [go-home-icon-button]
                      [order-by-icon-button]
                      [elements/button-separator {:indent {:vertical :xxs}}]
                      [create-folder-icon-button]
                      [upload-files-icon-button]]
                [:div {:style {:flex-grow "1"}}
                      [search-items-field]]]
          [elements/horizontal-separator {:height :xxl}]))

(defn- label-bar
  []
  (let [header-label @(r/subscribe [:item-browser/get-current-item-label :storage.media-selector])]
       [common/item-selector-label-bar :services.selector
                                       {:label    header-label
                                        :on-close [:x.ui/remove-popup! :storage.media-selector/view]}]))

(defn- header
  ; @param (keyword) popup-id
  [_]
  [:<> [label-bar]
       [control-bar]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) popup-id
  [popup-id]
  [popup-a/layout :storage.media-selector/view
                  {:body                #'body
                   :header              #'header
                   :footer              #'footer
                   :min-width           :m
                   :stretch-orientation :vertical}])
