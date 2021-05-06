package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class GT4500Test {

  private GT4500 ship;
  private TorpedoStore mockPrimary;
  private TorpedoStore mockSecondary;

  @BeforeEach
  public void init(){
    mockPrimary = mock(TorpedoStore.class);
    mockSecondary = mock(TorpedoStore.class);
    this.ship = new GT4500(mockPrimary, mockSecondary);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockSecondary.isEmpty()).thenReturn(false);
    when(mockPrimary.fire(1)).thenReturn(true);
    when(mockSecondary.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    verify(mockPrimary, times(1)).fire(1);
    verify(mockSecondary, times(0)).fire(1);

    // Assert
    assertEquals(true, result);
  }

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockSecondary.isEmpty()).thenReturn(false);
    when(mockPrimary.fire(1)).thenReturn(true);
    when(mockSecondary.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    verify(mockPrimary, times(1)).fire(1);
    verify(mockSecondary, times(1)).fire(1);

    // Assert
    assertEquals(true, result);
  }

  /*1: (Single) : For the first time the primary store is fired.
    The ship fires 1 torpedo in single fire mode. 
    Expected: only the primary torpedo store fires*/
    @Test
    public void primaryFirst_Single_Success(){
      // Arrange
      when(mockPrimary.isEmpty()).thenReturn(false);
      when(mockSecondary.isEmpty()).thenReturn(false);
      when(mockPrimary.fire(1)).thenReturn(true);
      when(mockSecondary.fire(1)).thenReturn(true);
  
      // Act
      boolean result = ship.fireTorpedo(FiringMode.SINGLE);
  
      verify(mockPrimary, times(1)).fire(1);
      verify(mockSecondary, times(0)).fire(1);
  
      // Assert
      assertEquals(true, result);
    }

    /*2: (Single) : The firing stores fire alternatively
    The ship fires 4 torpedoes
    Expected: The firing order will be: 1,2,1,2*/
    @Test
    public void alternating_Single_Success(){
      // Arrange
      when(mockPrimary.isEmpty()).thenReturn(false);
      when(mockSecondary.isEmpty()).thenReturn(false);
      when(mockPrimary.fire(1)).thenReturn(true);
      when(mockSecondary.fire(1)).thenReturn(true);
  
      boolean result = ship.fireTorpedo(FiringMode.SINGLE);
      verify(mockPrimary, times(1)).fire(1);
      verify(mockSecondary, times(0)).fire(1);
      assertEquals(true, result);

      result = ship.fireTorpedo(FiringMode.SINGLE);
      verify(mockPrimary, times(1)).fire(1);
      verify(mockSecondary, times(1)).fire(1);
      assertEquals(true, result);

      result = ship.fireTorpedo(FiringMode.SINGLE);
      verify(mockPrimary, times(2)).fire(1);
      verify(mockSecondary, times(1)).fire(1);
      assertEquals(true, result);

      result = ship.fireTorpedo(FiringMode.SINGLE);
      verify(mockPrimary, times(2)).fire(1);
      verify(mockSecondary, times(2)).fire(1);
      assertEquals(true, result);
    }

    /*3: (Single) : If the fired store reports a failure, the ship does not try to fire the other one.
    The ship tries to shoot with the primary store, but it fails
    Expected: no torpedoes are fired, neither from the first or secondary store*/
    @Test
    public void primaryFails_Single_Success(){
      // Arrange
      when(mockPrimary.isEmpty()).thenReturn(false);
      when(mockSecondary.isEmpty()).thenReturn(false);
      when(mockPrimary.fire(1)).thenReturn(false);
      when(mockSecondary.fire(1)).thenReturn(true);
  
      // Act
      boolean result = ship.fireTorpedo(FiringMode.SINGLE);
  
      verify(mockPrimary, times(1)).fire(1);
      verify(mockSecondary, times(0)).fire(1);
  
      // Assert
      assertEquals(false, result);
    }

    /*4: (Single) : If the store next in line is empty, the ship tries to fire the other store.
    The ship tries to fire a torpedo, but the current store is empty
    Expected: the ship fires from the other torpedo store*/
    @Test
    public void primaryEmptyShootSecondary_Single_Success(){
      // Arrange
      when(mockPrimary.isEmpty()).thenReturn(true);
      when(mockSecondary.isEmpty()).thenReturn(false);
      when(mockPrimary.fire(1)).thenReturn(true);
      when(mockSecondary.fire(1)).thenReturn(true);
  
      // Act
      boolean result = ship.fireTorpedo(FiringMode.SINGLE);
  
      verify(mockPrimary, times(0)).fire(1);
      verify(mockSecondary, times(1)).fire(1);
  
      // Assert
      assertEquals(true, result);
    }

    /*5: 5: (ALL) : tries to fire both of the torpedo stores.
    The ship fires from both torpedo store in all fire mode.
    Expected: torpedoes are fired from both stores*/
    @Test
    public void bothShoot_ALL_Success(){
      // Arrange
      when(mockPrimary.isEmpty()).thenReturn(false);
      when(mockSecondary.isEmpty()).thenReturn(false);
      when(mockPrimary.fire(1)).thenReturn(true);
      when(mockSecondary.fire(1)).thenReturn(true);
  
      // Act
      boolean result = ship.fireTorpedo(FiringMode.ALL);
  
      verify(mockPrimary, times(1)).fire(1);
      verify(mockSecondary, times(1)).fire(1);
  
      // Assert
      assertEquals(true, result);
    }

    /* +1:  although primary was fired last time, but the secondary is empty thus try to fire primary again
    The ship fires the primary store, and tries to fire again, but the secondary is empty
    Expected: the ship fires the primary store again
    */
    @Test
    public void secondaryEmptyShootPrimary_Single_Success(){
      // Arrange
      when(mockPrimary.isEmpty()).thenReturn(false);
      when(mockSecondary.isEmpty()).thenReturn(true);
      when(mockPrimary.fire(1)).thenReturn(true);
      when(mockSecondary.fire(1)).thenReturn(true);
  
      // Act
      boolean result = ship.fireTorpedo(FiringMode.SINGLE);
      verify(mockPrimary, times(1)).fire(1);
      verify(mockSecondary, times(0)).fire(1);
      assertEquals(true, result);

      result = ship.fireTorpedo(FiringMode.SINGLE);
      verify(mockPrimary, times(2)).fire(1);
      verify(mockSecondary, times(0)).fire(1);
      assertEquals(true, result);
    }

    @Test
    public void cover1_ALL_Success(){
      // Arrange
      when(mockPrimary.isEmpty()).thenReturn(true);
      when(mockSecondary.isEmpty()).thenReturn(true);
      when(mockPrimary.fire(1)).thenReturn(false);
      when(mockSecondary.fire(1)).thenReturn(true);
  
      // Act
      boolean result = ship.fireTorpedo(FiringMode.ALL);
      assertEquals(false, result);

    }

    @Test
    public void cover2_Single_Success(){
      // Arrange
      when(mockPrimary.isEmpty()).thenReturn(false);
      when(mockSecondary.isEmpty()).thenReturn(true);
      when(mockPrimary.fire(1)).thenReturn(true);
      when(mockSecondary.fire(1)).thenReturn(true);
  
      // Act
      boolean result = ship.fireTorpedo(FiringMode.SINGLE);
      assertEquals(true, result);

      when(mockPrimary.isEmpty()).thenReturn(true);
      result = ship.fireTorpedo(FiringMode.SINGLE);
      assertEquals(false, result);

    }

    @Test
    public void cover3_Mix_Success(){
      // Arrange
      when(mockPrimary.isEmpty()).thenReturn(false);
      when(mockSecondary.isEmpty()).thenReturn(false);
      when(mockPrimary.fire(1)).thenReturn(true);
      when(mockSecondary.fire(1)).thenReturn(true);
  
      // Act
      boolean result = ship.fireTorpedo(FiringMode.ALL);
      assertEquals(true, result);

      when(mockPrimary.isEmpty()).thenReturn(true);
      result = ship.fireTorpedo(FiringMode.SINGLE);
      assertEquals(true, result);

    }

    
    
}
