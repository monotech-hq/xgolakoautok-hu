
(ns app.pages.frontend.editor.views
    (:require [app.common.frontend.api  :as common]
              [app.storage.frontend.api :as storage]
              [elements.api             :as elements]
              [engines.item-editor.api  :as item-editor]
              [forms.api                :as forms]
              [layouts.surface-a.api    :as surface-a]
              [re-frame.api             :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  []
  (let [current-view-id @(r/subscribe [:gestures/get-current-view-id :pages.editor])]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  []
  [common/item-editor-menu-bar :pages.editor
                               {:menu-items []}])

(defn- controls
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :pages.editor])]
       [common/item-editor-controls :pages.editor
                                    {:disabled? editor-disabled?}]))

(defn- breadcrumbs
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :pages.editor])
        page-name        @(r/subscribe [:db/get-item [:pages :editor/edited-item :name]])
        page-id          @(r/subscribe [:router/get-current-route-path-param :item-id])
        page-uri          (str "/@app-home/pages/" page-id)]
       [common/surface-breadcrumbs :pages.editor/view
                                   {:crumbs (if page-id [{:label :app-home :route "/@app-home"}
                                                         {:label :pages    :route "/@app-home/pages"}
                                                         {:label page-name :route page-uri :placeholder :unnamed-page}
                                                         {:label :edit!}]
                                                        [{:label :app-home :route "/@app-home"}
                                                         {:label :pages    :route "/@app-home/pages"}
                                                         {:label :add!}])
                                    :disabled? editor-disabled?}]))

(defn- label
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :pages.editor])
        page-name        @(r/subscribe [:db/get-item [:pages :editor/edited-item :name]])]
       [common/surface-label :pages.editor/view
                             {:disabled?   editor-disabled?
                              :label       page-name
                              :placeholder :unnamed-page}]))

(defn- header
  []
  [:<> [:div {:style {:display "flex" :justify-content "space-between" :flex-wrap "wrap" :grid-row-gap "48px"}}
             [:div [label]
                   [breadcrumbs]]
             [:div [controls]]]
       [elements/horizontal-separator {:size :xxl}]
       [menu-bar]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-structure
  []
  [:<> [header]
       [body]])

(defn- page-editor
  []
  [item-editor/body :pages.editor
                    {:auto-title?      true
                     :form-element     #'view-structure
                     :error-element    [common/error-content {:error :the-item-you-opened-may-be-broken}]
                     :ghost-element    #'common/item-editor-ghost-element
                     :initial-item     {:visibility :public}
                     :item-path        [:pages :editor/edited-item]
                     :label-key        :name
                     :suggestion-keys  [:name]
                     :suggestions-path [:pages :editor/suggestions]}])

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:page #'page-editor}])
