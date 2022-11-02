
(ns app.storage.frontend.media-menu.views
    (:require [app.common.frontend.api :as common]
              [elements.api            :as elements]
              [io.api                  :as io]
              [layouts.popup-a.api     :as popup-a]
              [re-frame.api            :as r]))

;; -- Item-menu components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn media-menu-header
  [{:keys [alias] :as media-item}]
  [common/menu-header :storage.media-menu/view {:label alias}])

;; -- Directory-item menu components ------------------------------------------
;; ----------------------------------------------------------------------------

(defn directory-menu-body
  [directory-item]
  [:<> [elements/button ::open-directory-button
                        {:hover-color :highlight
                         :icon        :folder
                         :icon-family :material-icons-outlined
                         :indent      {:vertical :xs}
                         :label       :open!
                         :on-click    [:storage.media-browser/open-directory! directory-item]
                         :preset      :default}]
       [elements/button ::copy-directory-link-button
                        {:hover-color :highlight
                         :icon        :content_paste
                         :indent      {:vertical :xs}
                         :label       :copy-link!
                         :on-click    [:storage.media-browser/copy-directory-link! directory-item]
                         :preset      :default}]
;       [elements/button ::move-directory-button
;                        {:disabled?   true
;                         :hover-color :highlight
;                         :icon        :drive_file_move
;                         :icon-family :material-icons-outlined
;                         :indent      {:vertical :xs}
;                         :label       :move!
;                         :on-click    [:storage.media-browser/move-item! directory-item]
;                         :preset      :default}]
       [elements/button ::duplicate-directory-button
                        {:hover-color :highlight
                         :icon        :content_copy
                         :indent      {:vertical :xs}
                         :label       :duplicate!
                         :on-click    [:storage.media-browser/duplicate-item! directory-item]
                         :preset      :default}]
       [elements/button ::rename-directory-button
                        {:hover-color :highlight
                         :icon        :edit
                         :indent      {:vertical :xs}
                         :label       :rename!
                         :on-click    [:storage.media-browser/rename-item! directory-item]
                         :preset      :default}]
       [elements/button ::delete-directory-button
                        {:hover-color :highlight
                         :icon        :delete_outline
                         :indent      {:bottom :xs :vertical :xs}
                         :label       :delete!
                         :on-click    [:storage.media-browser/delete-item! directory-item]
                         :preset      :warning}]])

(defn directory-menu
  [directory-item]
  [popup-a/layout :storage.media-menu/view
                  {:body      [directory-menu-body directory-item]
                   :header    [media-menu-header   directory-item]
                   :min-width :xxs}])

;; -- File-item menu components -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-menu-body
  [{:keys [mime-type] :as file-item}]
  [:<> (if (or (io/mime-type->image? mime-type)
               (= mime-type "application/pdf"))
           [elements/button ::preview-file-button
                            {:hover-color :highlight
                             :icon :preview
                             :indent {:vertical :xs}
                             :label :file-preview
                             :on-click [:storage.media-browser/preview-file! file-item]
                             :preset :default}])
       [elements/button ::download-file-button
                        {:hover-color :highlight
                         :icon        :cloud_download
                         :indent      {:vertical :xs}
                         :label       :download!
                         :on-click    [:storage.media-browser/download-file! file-item]
                         :preset      :default}]
       [elements/button ::copy-file-link-button
                        {:hover-color :highlight
                         :icon        :content_paste
                         :indent      {:vertical :xs}
                         :label       :copy-link!
                         :on-click    [:storage.media-browser/copy-file-link! file-item]
                         :preset      :default}]
;       [elements/button ::move-file-button
;                        {:hover-color :highlight
;                         :icon        :drive_file_move
;                         :indent      {:vertical :xs}
;                         :label       :move!
;                         :icon-family :material-icons-outlined
;                         :on-click    [:storage.media-browser/move-item! file-item]
;                         :preset      :default
;                         ; TEMP
;                         :disabled? true}]
       [elements/button ::duplicate-file-button
                        {:hover-color :highlight
                         :icon        :content_copy
                         :indent      {:vertical :xs}
                         :label       :duplicate!
                         :on-click    [:storage.media-browser/duplicate-item! file-item]
                         :preset      :default}]
       [elements/button ::rename-file-button
                        {:hover-color :highlight
                         :icon        :edit
                         :indent      {:vertical :xs}
                         :label       :rename!
                         :on-click    [:storage.media-browser/rename-item! file-item]
                         :preset      :default}]
       [elements/button ::delete-file-button
                        {:hover-color :highlight
                         :icon        :delete_outline
                         :indent      {:bottom :xs :vertical :xs}
                         :label       :delete!
                         :on-click    [:storage.media-browser/delete-item! file-item]
                         :preset      :warning}]])

(defn file-menu
  [file-item]
  [popup-a/layout :storage.media-menu/view
                  {:body      [file-menu-body    file-item]
                   :header    [media-menu-header file-item]
                   :min-width :xxs}])
