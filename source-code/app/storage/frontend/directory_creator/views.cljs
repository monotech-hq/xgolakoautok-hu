
(ns app.storage.frontend.directory-creator.views
    (:require [elements.api        :as elements]
              [io.api              :as io]
              [layouts.popup-a.api :as popup-a]
              [re-frame.api        :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cancel-button
  [creator-id]
  [elements/button ::cancel-button
                   {:font-size   :xs
                    :hover-color :highlight
                    :indent      {:horizontal :xxs :vertical :xxs}
                    :keypress    {:key-code 27 :required? true}
                    :label       :cancel!
                    :on-click    [:x.ui/remove-popup! :storage.directory-creator/view]}])

(defn header-label
  [creator-id]
  [elements/label ::header-label
                  {:content     :create-directory!
                   :line-height :block}])

(defn create-button
  [creator-id]
  [elements/button ::create-button
                   {:color       :primary
                    :font-size   :xs
                    :hover-color :highlight
                    :indent      {:horizontal :xxs :vertical :xxs}
                    :keypress    {:key-code 13 :required? true}
                    :label       :create!
                    :on-click    [:storage.directory-creator/create-directory! creator-id]}])

(defn header
  [creator-id]
  [elements/horizontal-polarity ::header
                                {:start-content  [cancel-button creator-id]
                                 :middle-content [header-label creator-id]
                                 :end-content    [create-button creator-id]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn directory-name-field
  [creator-id]
  [elements/text-field ::directory-name-field
                       {:autoclear?  true
                        :autofocus?  true
                        :indent      {:bottom :xs :top :m :vertical :xs}
                        :placeholder :directory-name
                        :validator   {:f               io/directory-name-valid?
                                      :invalid-message :invalid-name
                                      :pre-validate?   true}
                        :value-path  [:storage :directory-creator/meta-items :directory-name]}])

(defn body
  [creator-id]
  [directory-name-field creator-id])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  [creator-id]
  [popup-a/layout :storage.directory-creator/view
                  {:body      [body   creator-id]
                   :header    [header creator-id]
                   :min-width :s}])
