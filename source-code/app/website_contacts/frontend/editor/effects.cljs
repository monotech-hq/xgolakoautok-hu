
(ns app.website-contacts.frontend.editor.effects
  (:require [app.website-contacts.frontend.editor.views :as editor.views]
            [re-frame.api                               :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :website-contacts.editor/load-editor!
  {:dispatch-n [[:x.gestures/init-view-handler! :website-contacts.editor
                                                {:default-view-id :contacts-data}]
                [:website-contacts.editor/render-editor!]]})

(r/reg-event-fx :website-contacts.editor/render-editor!
  [:x.ui/render-surface! :website-contacts.editor/view
                         {:content #'editor.views/view}])
