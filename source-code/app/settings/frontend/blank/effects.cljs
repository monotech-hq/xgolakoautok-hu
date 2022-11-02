
(ns app.settings.frontend.blank.effects
    (:require [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :settings.editor/load-editor!
  [:views.blank-page/load-page! :settings.editor/view
                                {:title :settings :helper :there-is-no-available-settings}])
