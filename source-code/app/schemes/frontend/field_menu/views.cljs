
(ns app.schemes.frontend.field-menu.views
    (:require [app.common.frontend.api :as common]
              [elements.api            :as elements]
              [layouts.popup-a.api     :as popup-a]
              [re-frame.api            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- delete-field-button
  ; @param (keyword) scheme-id
  ; @param (keyword) field-id
  [scheme-id field-id]
  (let [{:field/keys [undeletable?]} @(r/subscribe [:schemes.form-handler/get-scheme-field scheme-id field-id])]
       [elements/button ::delete-field-button
                        {:color       :warning
                         :disabled?   undeletable?
                         :horizontal-align :left
                         :hover-color :highlight
                         :icon        :delete_outline
                         :indent      {:bottom :xs :vertical :xs}
                         :label       :delete-field!
                         :on-click    [:schemes.field-menu/delete-field! scheme-id field-id]}]))

(defn- edit-field-button
  ; @param (keyword) scheme-id
  ; @param (keyword) field-id
  [scheme-id field-id]
  [elements/button ::edit-field-button
                   {:horizontal-align :left
                    :hover-color :highlight
                    :icon        :edit
                    :indent      {:vertical :xs}
                    :label       :edit-field!
                    :on-click    [:schemes.field-menu/edit-field! scheme-id field-id]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  ; @param (keyword) scheme-id
  ; @param (keyword) field-id
  [scheme-id field-id]
  (let [{:field/keys [name]} @(r/subscribe [:schemes.form-handler/get-scheme-field scheme-id field-id])]
       [:<> [common/menu-header :schemes.field-menu/view {:label name}]
            [edit-field-button   scheme-id field-id]
            [delete-field-button scheme-id field-id]]))

(defn view
  ; @param (keyword) scheme-id
  ; @param (keyword) field-id
  [scheme-id field-id]
  [popup-a/layout :schemes.field-menu/view
                  {:body [body scheme-id field-id]
                   :min-width :xxs}])
