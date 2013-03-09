package de.opitzconsulting.jbinding;

import static de.opitzconsulting.jbinding.Binder.bind;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import javax.swing.JLabel;

import org.junit.Test;

import de.opitzconsulting.jbinding.bom.Person;

public class SimpleBindingTest {

    @Test
    public void createSimpleBinding() {
        Person person = new Person();
        person.setName("Terry");
        JLabel nameLabel = new JLabel();
        bind(nameLabel).to(person).getName();
        assertThat(nameLabel.getText(), equalTo(person.getName()));
    }

}
