
(ns app.common.frontend.item-viewer.views
    (:require [app.common.frontend.item-editor.views :as item-editor.views]
              [app.common.frontend.surface.views     :as surface.views]
              [app.components.frontend.api           :as components]
              [elements.api                          :as elements]
              [re-frame.api                          :as r]
              [vector.api                            :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-viewer-item-modified
  ; @param (keyword) viewer-id
  ; @param (map) info-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false}
  ;
  ; @usage
  ;  [item-viewer-item-modified :my-viewer {...}]
  [viewer-id {:keys [disabled?]}]
  (let [current-item   @(r/subscribe [:item-viewer/get-current-item viewer-id])
        added-at        (-> current-item :modified-at)
        user-first-name (-> current-item :modified-by :user-profile/first-name)
        user-last-name  (-> current-item :modified-by :user-profile/last-name)
        user-full-name @(r/subscribe [:x.locales/get-ordered-name user-first-name user-last-name])
        timestamp      @(r/subscribe [:x.activities/get-actual-timestamp added-at])
        modified        {:content :last-modified-n :replacements [(str user-full-name ", " timestamp)]}
        modified        {:content :last-modified-n :replacements [timestamp]}]
       [components/surface-description ::item-viewer-item-modified
                                       {:content   modified
                                        :disabled? disabled?}]))

(defn item-viewer-item-created
  ; @param (keyword) viewer-id
  ; @param (map) info-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false}
  ;
  ; @usage
  ;  [item-viewer-item-created :my-viewer {...}]
  [viewer-id {:keys [disabled?]}]
  (let [current-item   @(r/subscribe [:item-viewer/get-current-item viewer-id])
        added-at        (-> current-item :added-at)
        user-first-name (-> current-item :added-by :user-profile/first-name)
        user-last-name  (-> current-item :added-by :user-profile/last-name)
        user-full-name @(r/subscribe [:x.locales/get-ordered-name user-first-name user-last-name])
        timestamp      @(r/subscribe [:x.activities/get-actual-timestamp added-at])
        created         {:content :created-n :replacements [(str user-full-name ", " timestamp)]}]
       [components/surface-description ::item-viewer-item-modified
                                       {:content   created
                                        :disabled? disabled?}]))

(defn item-viewer-item-info
  ; @param (keyword) viewer-id
  ; @param (map) info-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :indent (map)}
  ;
  ; @usage
  ;  [item-viewer-item-info :my-viewer {...}]
  [viewer-id {:keys [indent] :as info-props}]
  [elements/blank ::item-viewer-item-info
                  {:content [:div {:style {:display :flex :flex-direction :column :align-items :center :justify-content :center}}
                                  ;[item-viewer-item-created  viewer-id info-props]
                                  [item-viewer-item-modified viewer-id info-props]]
                   :indent  indent}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-viewer-color-stamp
  ; @param (keyword) viewer-id
  ; @param (map) marker-props
  ;  {:disabled? (boolean)(opt)
  ;   :indent (map)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :value-path (vector)}
  ;
  ; @usage
  ;  [item-viewer-color-stamp :my-viewer {...}]
  [_ {:keys [disabled? indent label value-path]}]
  (let [picked-colors @(r/subscribe [:x.db/get-item value-path])]
       [elements/color-stamp {:colors    picked-colors
                              :disabled? disabled?
                              :indent    indent
                              :label     label
                              :size      :xxl}]))

;; -- Menu-bar components -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-viewer-menu-bar
  ; A komponens használatához ne felejts el inicializálni egy gestures/view-handler
  ; kezelőt, a viewer-id azonosítóval!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) bar-props
  ;  {:disabled? (boolean)(opt)
  ;   :menu-items (maps in vector)
  ;    [{:label (metamorphic-content)}]}
  ;
  ; @usage
  ;  [item-viewer-menu-bar :my-viewer {...}]
  [viewer-id {:keys [disabled? menu-items] :as bar-props}]
  (letfn [(f [menu-items menu-item] (conj menu-items (item-editor.views/item-editor-menu-item-props viewer-id bar-props menu-item)))]
         [:<> [elements/menu-bar ::item-viewer-menu-bar {:disabled?  disabled?
                                                         :menu-items (reduce f [] menu-items)}]]))
              ;[elements/horizontal-line {:color :highlight :indent {:vertical :s}}]]))

;; -- Ghost-view components ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-viewer-ghost-element
  ; @param (keyword) viewer-id
  ; @param (map) element-props
  ;
  ; @usage
  ;  [item-viewer-ghost-element :my-viewer {...}]
  [viewer-id _]
  [surface.views/surface-box-layout-ghost-view viewer-id {:breadcrumb-count 3}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-item-button
  ; @param (keyword) viewer-id
  ; @param (map) bar-props
  ;  {:disabled? (boolean)(opt)}
  ;
  ; @usage
  ;  [delete-item-button :my-viewer {...}]
  [viewer-id {:keys [disabled?]}]
  [components/surface-button ::delete-item-button
                             {:color       :warning
                              :disabled?   disabled?
                              :hover-color :highlight
                              :icon        :delete_outline
                              :label       :delete!
                              :on-click    [:item-viewer/delete-item! viewer-id]}])

(defn duplicate-item-button
  ; @param (keyword) viewer-id
  ; @param (map) bar-props
  ;  {:disabled? (boolean)(opt)}
  ;
  ; @usage
  ;  [duplicate-item-button :my-viewer {...}]
  [viewer-id {:keys [disabled?]}]
  [components/surface-button ::duplicate-item-button
                             {:disabled?   disabled?
                              :hover-color :highlight
                              :icon        :file_copy
                              :icon-family :material-icons-outlined
                              :label       :duplicate!
                              :on-click    [:item-viewer/duplicate-item! viewer-id]}])

(defn edit-item-button
  ; @param (keyword) viewer-id
  ; @param (map) bar-props
  ;  {:disabled? (boolean)(opt)
  ;   :edit-item-uri (string)}
  ;
  ; @usage
  ;  [edit-item-button :my-viewer {...}]
  [viewer-id {:keys [disabled? edit-item-uri]}]
  [components/surface-button ::edit-item-button
                             {:background-color "#5a4aff"
                              :color            "white"
                              :disabled?        disabled?
                              :icon             :edit
                              :label            :edit!
                              :on-click         [:x.router/go-to! edit-item-uri]}])

(defn item-viewer-controls
  ; @param (keyword) viewer-id
  ; @param (map) bar-props
  ;  {:disabled? (boolean)(opt)
  ;   :edit-item-uri (string)}
  ;
  ; @usage
  ;  [item-viewer-controls :my-viewer {...}]
  [viewer-id bar-props]
  [:div {:style {:display "flex" :grid-column-gap "12px"}}
        [:<> [delete-item-button    viewer-id bar-props]
             [duplicate-item-button viewer-id bar-props]
             [edit-item-button      viewer-id bar-props]]])
