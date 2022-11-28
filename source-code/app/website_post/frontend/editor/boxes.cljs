
(ns app.website-post.frontend.editor.boxes
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [app.storage.frontend.api    :as storage]
              [elements.api                :as elements]
              [forms.api                   :as forms]
              [re-frame.api                :as r]))

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
       [components/surface-box ::server-data-box
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
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :server-data}]))

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
       [components/surface-box ::basic-data-box
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
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :basic-data}]))
