
(ns app.storage.frontend.media-selector.views
    (:require [app.common.frontend.api          :as common]
              [app.components.frontend.api      :as components]
              [app.storage.frontend.core.config :as core.config]
              [elements.api                     :as elements]
              [engines.item-browser.api         :as item-browser]
              [format.api                       :as format]
              [io.api                           :as io]
              [layouts.popup-a.api              :as popup-a]
              [re-frame.api                     :as r]
              [x.components.api                 :as x.components]
              [x.media.api                      :as x.media]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
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

(defn- directory-item-structure
  [selector-id item-dex {:keys [alias size items modified-at]}]
  (let [timestamp  @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
        item-last? @(r/subscribe [:item-lister/item-last? selector-id item-dex])
        size        (str (-> size io/B->MB format/decimals (str " MB\u00A0\u00A0\u00A0|\u00A0\u00A0\u00A0"))
                         (x.components/content {:content :n-items :replacements [(count items)]}))
        icon-family (if (empty? items) :material-icons-outlined :material-icons-filled)]
       [common/list-item-structure {:cells [[    {:icon :folder :icon-family icon-family}]
                                            [common/list-item-primary-cell {:label alias :description size :timestamp timestamp :stretch? true}]
                                            [components/list-item-marker       {:icon :navigate_next}]]
                                    :separator (if-not item-last? :bottom)}]))

(defn- directory-item
  [selector-id item-dex {:keys [id] :as directory-item}]
  [elements/toggle {:content     [directory-item-structure selector-id item-dex directory-item]
                    :hover-color :highlight
                    :on-click    [:item-browser/browse-item! :storage.media-selector id]}])

(defn- file-item-structure
  [selector-id item-dex {:keys [alias id modified-at filename size]}]
  (let [timestamp  @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
        item-last? @(r/subscribe [:item-lister/item-last? selector-id item-dex])
        size        (-> size io/B->MB format/decimals (str " MB"))]
       [common/list-item-structure {:cells [[ (if (io/filename->image? alias)
                                                                            {:thumbnail (x.media/filename->media-thumbnail-uri filename)}
                                                                            {:icon :insert_drive_file :icon-family :material-icons-outlined})]
                                            [common/list-item-primary-cell {:label alias :description size :timestamp timestamp :stretch? true}]
                                            [common/selector-item-marker   selector-id item-dex {:item-id id}]]
                                    :separator (if-not item-last? :bottom)}]))

(defn- file-item
  [selector-id item-dex {:keys [id] :as file-item}]
  (let [file-selectable? @(r/subscribe [:storage.media-selector/file-selectable? file-item])]
       [elements/toggle {:content     [file-item-structure selector-id item-dex file-item]
                         :disabled?   (not file-selectable?)
                         :hover-color :highlight
                         :on-click    [:item-selector/item-clicked :storage.media-selector id]}]))
(defn- media-item
  [selector-id item-dex {:keys [mime-type] :as media-item}]
  (case mime-type "storage/directory" [directory-item selector-id item-dex media-item]
                                      [file-item      selector-id item-dex media-item]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- media-list
  []
  (let [items @(r/subscribe [:item-browser/get-downloaded-items :storage.media-selector])]
       [common/item-list :storage.media-selector {:item-element #'media-item :items items}]))

(defn- media-browser
  []
  [item-browser/body :storage.media-selector
                     {:default-item-id  core.config/ROOT-DIRECTORY-ID
                      :default-order-by :modified-at/descending
                      :item-path        [:storage :media-selector/browsed-item]
                      :items-path       [:storage :media-selector/downloaded-items]
                      :error-element    [components/error-content {:error :the-content-you-opened-may-be-broken}]
                      :ghost-element    [common/item-selector-ghost-element]
                      :list-element     [media-list]
                      :items-key        :items
                      :label-key        :alias
                      :path-key         :path}])

(defn- body
  []
  [:<> [elements/horizontal-separator {:size :xs}]
       [media-browser]])

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
  [:div {:style {:display "flex"}}
        [:div {:style {:display "flex"}}
              [go-up-icon-button]
              [go-home-icon-button]
              [order-by-icon-button]
              [elements/button-separator {:indent {:vertical :xxs}}]
              [create-folder-icon-button]
              [upload-files-icon-button]]
        [:div {:style {:flex-grow "1"}}
              [search-items-field]]])

(defn- header
  [selector-id]
  (let [header-label @(r/subscribe [:item-browser/get-current-item-label :storage.media-selector])]
       [:<> [components/popup-label-bar :storage.media-selector/view
                                        {:primary-button   {:label :save! :on-click [:item-selector/save-selection! :storage.media-selector]}
                                         :secondary-button (if-let [autosaving? @(r/subscribe [:item-selector/autosaving? :storage.media-selector])]
                                                                   {:label :abort!  :on-click [:item-selector/abort-autosave! :storage.media-selector]}
                                                                   {:label :cancel! :on-click [:x.ui/remove-popup! :storage.media-selector/view]})
                                         :label header-label}]
            (if-let [first-data-received? @(r/subscribe [:item-browser/first-data-received? :storage.media-selector])]
                    [control-bar]
                    [elements/horizontal-separator {:size :xxl}])]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  [selector-id]
  [popup-a/layout :storage.media-selector/view
                  {:body                #'body
                   :header              #'header
                   :footer              #'footer
                   :min-width           :m
                   :stretch-orientation :vertical}])
