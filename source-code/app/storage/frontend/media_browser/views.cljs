
(ns app.storage.frontend.media-browser.views
    (:require [app.common.frontend.api                    :as common]
              [app.components.frontend.api                :as components]
              [app.storage.frontend.core.config           :as core.config]
              [app.storage.frontend.media-browser.helpers :as media-browser.helpers]
              [elements.api                               :as elements]
              [engines.item-browser.api                   :as item-browser]
              [format.api                                 :as format]
              [io.api                                     :as io]
              [layouts.surface-a.api                      :as surface-a]
              [keyword.api                                :as keyword]
              [re-frame.api                               :as r]
              [x.components.api                           :as x.components]
              [x.media.api                                :as x.media]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  (if-let [first-data-received? @(r/subscribe [:item-browser/first-data-received? :storage.media-browser])]
          [common/item-lister-download-info :storage.media-browser {}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- directory-info
  []
  (let [size  @(r/subscribe [:x.db/get-item [:storage :media-browser/browsed-item :size]])
        items @(r/subscribe [:x.db/get-item [:storage :media-browser/browsed-item :items]])
        directory-info (str (-> size io/B->MB format/decimals (str " MB\u00A0\u00A0\u00A0|\u00A0\u00A0\u00A0"))
                            (x.components/content {:content :n-items :replacements [(count items)]}))]
       [components/surface-description ::directory-info
                                       {:content          directory-info
                                        :indent           {:top :m :right :xs}
                                        :horizontal-align :right}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn directory-item-structure
  [browser-id item-dex {:keys [alias size id items modified-at]}]
  (let [timestamp  @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
        item-last? @(r/subscribe [:item-browser/item-last? browser-id item-dex])
        item-count  (x.components/content {:content :n-items :replacements [(count items)]})
        size        (-> size io/B->MB format/decimals (str " MB"))
        icon-family (if (empty? items) :material-icons-outlined :material-icons-filled)]
       [common/list-item-structure {:cells [[ {:icon :folder :icon-family icon-family}]
                                            [common/list-item-label     {:content alias :stretch? true}]
                                            [common/list-item-details   {:contents [size item-count] :width "160px"}]
                                            [common/list-item-detail    {:content timestamp :width "160px"}]
                                            [components/list-item-marker    {:icon :navigate_next}]]
                                    :separator (if-not item-last? :bottom)}]))

(defn directory-item
  [browser-id item-dex {:keys [id] :as directory-item}]
  [elements/toggle {:content        [directory-item-structure browser-id item-dex directory-item]
                    :hover-color    :highlight
                    :on-click       [:item-browser/browse-item! :storage.media-browser id]
                    :on-right-click [:storage.media-menu/render-directory-menu! directory-item]}])

(defn file-item-structure
  [browser-id item-dex {:keys [alias id modified-at filename size] :as file-item}]
  (let [timestamp  @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
        item-last? @(r/subscribe [:item-browser/item-last? browser-id item-dex])
        size        (-> size io/B->MB format/decimals (str " MB"))]
       [common/list-item-structure {:cells [[ (if (io/filename->image? alias)
                                                                            {:thumbnail (x.media/filename->media-thumbnail-uri filename)}
                                                                            {:icon :insert_drive_file :icon-family :material-icons-outlined})]
                                            [common/list-item-label  {:content alias     :stretch? true}]
                                            [common/list-item-detail {:content size      :width "160px"}]
                                            [common/list-item-detail {:content timestamp :width "160px"}]
                                            [components/list-item-marker {:icon :more_vert}]]
                                    :separator (if-not item-last? :bottom)}]))

(defn file-item
  [browser-id item-dex file-item]
  [elements/toggle {:content        [file-item-structure browser-id item-dex file-item]
                    :hover-color    :highlight
                    :on-click       [:storage.media-menu/render-file-menu! file-item]
                    :on-right-click [:storage.media-menu/render-file-menu! file-item]}])

(defn media-item
  [browser-id item-dex {:keys [mime-type] :as media-item}]
  (case mime-type "storage/directory" [directory-item browser-id item-dex media-item]
                                      [file-item      browser-id item-dex media-item]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- media-list
  []
  (let [items @(r/subscribe [:item-browser/get-downloaded-items :storage.media-browser])]
       [common/item-list :storage.media-browser {:item-element #'media-item :items items}]))

(defn media-browser-body
  []
  [item-browser/body :storage.media-browser
                     {:auto-title?      true
                      :default-item-id   core.config/ROOT-DIRECTORY-ID
                      :default-order-by :modified-at/descending
                      :item-path        [:storage :media-browser/browsed-item]
                      :items-path       [:storage :media-browser/downloaded-items]
                      :error-element    [components/error-content {:error :the-content-you-opened-may-be-broken}]
                      :ghost-element    [common/item-lister-ghost-element]
                      :list-element     [media-list]
                      :items-key        :items
                      :label-key        :alias
                      :path-key         :path}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn upload-files-button
  []
  (let [browser-disabled? @(r/subscribe [:item-browser/browser-disabled? :storage.media-browser])]
       [elements/button ::upload-files-button
                        {:border-radius :s
                         :disabled?     browser-disabled?
                         :font-size     :xs
                         :hover-color   :highlight
                         :icon          :upload_file
                         :indent        {:right :xs :top :xs}
                         :label         :upload-file!
                         :on-click      [:storage.media-browser/upload-files!]}]))

(defn create-folder-button
  []
  (let [browser-disabled? @(r/subscribe [:item-browser/browser-disabled? :storage.media-browser])]
       [elements/button ::create-folder-button
                        {:border-radius :s
                         :disabled?     browser-disabled?
                         :font-size     :xs
                         :hover-color   :highlight
                         :indent        {:right :xs :top :xs}
                         :on-click      [:storage.media-browser/create-directory!]
                         :icon          :create_new_folder
                         :icon-family   :material-icons-outlined
                         :label         :create-directory!}]))

(defn go-home-icon-button
  []
  (let [browser-disabled? @(r/subscribe [:item-browser/browser-disabled? :storage.media-browser])
        at-home?          @(r/subscribe [:item-browser/at-home?          :storage.media-browser])
        error-mode?       @(r/subscribe [:item-browser/get-meta-item     :storage.media-browser :error-mode?])]
       [elements/icon-button ::go-home-icon-button
                             {:disabled?     (or browser-disabled? (and at-home? (not error-mode?)))
                              :border-radius :s
                              :hover-color   :highlight
                              :indent        {:top :xxs}
                              :on-click      [:item-browser/go-home! :storage.media-browser]
                              :preset        :home}]))

(defn go-up-icon-button
  []
  (let [browser-disabled? @(r/subscribe [:item-browser/browser-disabled? :storage.media-browser])
        at-home?          @(r/subscribe [:item-browser/at-home?          :storage.media-browser])]
       [elements/icon-button ::go-up-icon-button
                             {:disabled?     (or browser-disabled? at-home?)
                              :border-radius :s
                              :hover-color   :highlight
                              :indent        {:top :xxs}
                              :on-click      [:item-browser/go-up! :storage.media-browser]
                              :preset        :back}]))


(defn control-bar
  []
  [elements/horizontal-polarity ::control-bar
                                {:start-content [:<> [go-up-icon-button]
                                                     [go-home-icon-button]]
                                 :end-content   [:<> [create-folder-button]
                                                     [upload-files-button]]}])

(defn media-browser-header
  []
  [common/item-lister-header :storage.media-browser
                             {:cells [[common/item-lister-header-spacer :storage.media-browser {:width "108px"}]
                                      [common/item-lister-header-cell   :storage.media-browser {:label :name          :order-by-key :name :stretch? true}]
                                      [common/item-lister-header-cell   :storage.media-browser {:label :size          :order-by-key :size        :width "160px"}]
                                      [common/item-lister-header-cell   :storage.media-browser {:label :last-modified :order-by-key :modified-at :width "160px"}]
                                      [common/item-lister-header-spacer :storage.media-browser {:width "36px"}]]
                              :control-bar [control-bar]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  []
  [common/item-lister-wrapper :storage.media-browser
                              {:body   #'media-browser-body
                               :header #'media-browser-header}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- search-field
  []
  (let [browser-disabled? @(r/subscribe [:item-browser/browser-disabled? :storage.media-browser])]
       [common/item-browser-search-field :storage.media-browser
                                         {:disabled?   browser-disabled?
                                          :placeholder :search-in-the-directory
                                          :search-keys [:alias]}]))

(defn- search-description
  []
  (let [lister-disabled? @(r/subscribe [:item-browser/browser-disabled? :storage.media-browser])]
       [common/item-browser-search-description :storage.media-browser
                                               {:disabled? lister-disabled?}]))

(defn- breadcrumbs
  []
  (let [browser-disabled? @(r/subscribe [:item-browser/browser-disabled? :storage.media-browser])]
       [components/surface-breadcrumbs ::breadcrumbs
                                        {:crumbs [{:label :app-home :route "/@app-home"}
                                                  {:label :storage}]
                                         :disabled? browser-disabled?}]))

(defn- label
  []
  (let [browser-disabled? @(r/subscribe [:item-browser/browser-disabled? :storage.media-browser])
        directory-alias @(r/subscribe [:item-browser/get-current-item-label :storage.media-browser])]
       [components/surface-label ::label
                                 {:disabled? browser-disabled?
                                  :label     directory-alias}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  (if-let [first-data-received? @(r/subscribe [:item-browser/first-data-received? :storage.media-browser])]
          [:<> [label]
               [breadcrumbs]
               [search-field]
               [:div {:style {:display :flex :justify-content :space-between}}
                     [search-description]
                     [directory-info]]]
          [common/item-lister-ghost-header :parts.part-lister {}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-structure
  []
  [:<> [header]
       [body]
       [footer]])

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'view-structure}])
