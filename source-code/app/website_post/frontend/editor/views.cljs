
(ns app.website-post.frontend.editor.views
    (:require [app.common.frontend.api   :as common]
              [app.contents.frontend.api :as contents]
              [app.storage.frontend.api  :as storage]
              [css.api                   :as css]
              [elements.api              :as elements]
              [engines.file-editor.api   :as file-editor]
              [forms.api                 :as forms]
              [layouts.surface-a.api     :as surface-a]
              [vector.api         :as vector]
              [re-frame.api              :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- account-username-field
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-post.editor])]
       [elements/text-field ::account-username-field
                            {:disabled?   editor-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :username
                             :value-path  [:website-post :editor/edited-item :account-username]}]))

(defn- account-password-field
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-post.editor])]
       [elements/password-field ::account-password-field
                                {:disabled?   editor-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :password
                                 :value-path  [:website-post :editor/edited-item :account-password]}]))

(defn- email-server-port-field
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-post.editor])]
       [elements/text-field ::email-server-port-field
                            {:disabled?   editor-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :email-server-port
                             :placeholder "420"
                             :value-path  [:website-post :editor/edited-item :email-server-port]}]))

(defn- email-server-address-field
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-post.editor])]
       [elements/text-field ::email-server-address-field
                            {:disabled?   editor-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :email-server-address
                             :placeholder :server-address-placeholder
                             :value-path  [:website-post :editor/edited-item :email-server-address]}]))

(defn- server-data-box
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-post.editor])]
       [common/surface-box ::server-data-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 50})
                                                      [email-server-address-field]]
                                                [:div (forms/form-block-attributes {:ratio 50})
                                                      [email-server-port-field]]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 50})
                                                      [account-username-field]]
                                                [:div (forms/form-block-attributes {:ratio 50})
                                                      [account-password-field]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :server-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- server-data
  []
  [:<> [server-data-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- email-body-field
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-post.editor])]
       [elements/multiline-field ::email-body-field
                                 {:disabled?   editor-disabled?
                                  :label       :email-body
                                  :indent      {:top :m :vertical :s}
                                  :info-text   :email-body-helper
                                  :placeholder :email-body-placeholder
                                  :value-path  [:website-post :editor/edited-item :email-body]}]))

(defn- email-subject-field
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-post.editor])]
       [elements/text-field ::email-subject-field
                            {:disabled?   editor-disabled?
                             :label       :email-subject
                             :indent      {:top :m :vertical :s}
                             :info-text   :email-subject-helper
                             :placeholder :email-subject-placeholder
                             :value-path  [:website-post :editor/edited-item :email-subject]}]))

(defn- receiver-email-address-field
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-post.editor])]
       [elements/text-field ::receiver-email-address-field
                            {:disabled?   editor-disabled?
                             :label       :receiver-email-address
                             :indent      {:top :m :vertical :s}
                             :placeholder :email-address-placeholder
                             :value-path  [:website-post :editor/edited-item :receiver-email-address]}]))

(defn- basic-data-box
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-post.editor])]
       [common/surface-box ::basic-data-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 50})
                                                      [receiver-email-address-field]]
                                                [:div (forms/form-block-attributes {:ratio 50})]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [email-subject-field]]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [email-body-field]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :basic-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- basic-data
  []
  [:<> [basic-data-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-post.editor])]
       [common/file-editor-menu-bar :website-post.editor
                                    {:menu-items [{:label :basic-data  :change-keys [:receiver-email-address]}
                                                  {:label :server-data :change-keys [:email-server-address]}]
                                     :disabled? editor-disabled?}]))

(defn- body
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :website-post.editor])]
       (case current-view-id :basic-data  [basic-data]
                             :server-data [server-data])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- controls
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-post.editor])]
       [common/file-editor-controls :website-post.editor
                                    {:disabled? editor-disabled?}]))

(defn- breadcrumbs
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-post.editor])]
       [common/surface-breadcrumbs :website-post.editor/view
                                   {:crumbs [{:label :app-home :route "/@app-home"}
                                             {:label :website-post}]
                                    :disabled? editor-disabled?}]))

(defn- label
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-post.editor])]
       [common/surface-label :website-post.editor/view
                             {:disabled? editor-disabled?
                              :label     :website-post}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  [:<> [:div {:style {:display "flex" :justify-content "space-between" :flex-wrap "wrap" :grid-row-gap "48px"}}
             [:div [label]
                   [breadcrumbs]]
             [:div [controls]]]
       [elements/horizontal-separator {:size :xxl}]
       [menu-bar]])

(defn- view-structure
  []
  [:<> [header]
       [body]])

(defn- website-content-editor
  ; @param (keyword) surface-id
  [_]
  [file-editor/body :website-post.editor
                    {:content-path  [:website-post :editor/edited-item]
                     :form-element  #'view-structure
                     :error-element [common/error-content {:error :the-content-you-opened-may-be-broken}]
                     :ghost-element #'common/file-editor-ghost-element}])

(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'website-content-editor}])
